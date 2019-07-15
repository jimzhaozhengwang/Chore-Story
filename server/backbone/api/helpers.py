import itertools
from datetime import timedelta, datetime
from os.path import join

from os import listdir
from uuid import uuid4

from flask_login import current_user
from sqlalchemy import inspect

from ..exceptions import BackboneException
from ..models import Parent, Child

ALLOWED_EXTENSIONS = ['jpg', 'jpeg', 'png', 'gif']


def generate_prnt_resp(prnt):
    """
    Generate a dictionary description of a Parent object, used by for example /me
    :param prnt: a Parent object
    :return: a dictionary of description of Parent object
    """
    d = {'children': [{"id": p.id, "name": p.name} for p in prnt.clan.children],
         'clan_name': prnt.clan.name}
    for e in ['email', 'name', 'id', 'picture']:
        d[e] = getattr(prnt, e)
    return d


def generate_chd_resp(chd):
    """
    Generate a dictionary description of a Child object, used by for example /me
    :param chd: a Child object
    :return: a dictionary of description of Child object
    """
    d = {'clan_name': chd.clan.name,
         'parents': [{"id": p.id, "name": p.name, 'picture': p.picture} for p in chd.clan.parents]}
    for e in ['level', 'name', 'id', 'xp', 'username']:
        d[e] = getattr(chd, e)
    return d


def generate_qst_resp(qst, ts=datetime.utcnow()):
    """
    Generate a dictionary description of a Quest object, used by for example /get_quest
    :param qst: a Quest object
    :param ts: a datetime object that we want to get next occurrence from
    :return: a dictionary of description of Quest object
    """
    d = {}
    for e in ['title', 'description', 'reward', 'due', 'id', 'recurring', 'needs_verification']:
        d[e] = getattr(qst, e)
    # Add extra values that we don't store in database
    looking_for = find_next_time(qst, ts) if qst.recurring else qst.due
    d['due'] = datetime.timestamp(d['due'])
    if qst.recurring:
        d['next_occurrence'] = datetime.timestamp(looking_for)
    filtered_completions = list(filter(lambda c: c.value == looking_for, qst.completions))
    d['completed_on'] = filtered_completions[0].ts if len(filtered_completions) == 1 else None
    filtered_verifications = list(filter(lambda c: c.value == looking_for, qst.verifications))
    d['verified_on'] = filtered_verifications[0].ts if len(filtered_verifications) == 1 else None
    return d


def generate_clan_resp(clan):
    """
    Generate a dictionary description of a Quest object, used by for example /get_quest
    :param clan: clan object
    :return: a dictionary of description of Clan object
    """
    d = {"parents": [{"id": p.id, "name": p.name, "picture": p.picture} for p in clan.parents],
         "children": [{"id": c.id, "name": c.name} for c in clan.children]}
    for e in ['id', 'name']:
        d[e] = getattr(clan, e)
    return d


def child_is_my_child(child_obj):
    """Checks whether child is a child of the logged in parent"""
    return child_obj and isinstance(inspect(current_user).object, Parent) and child_obj in current_user.clan.children


def child_is_me(child_obj):
    """Checks whether child is currently logged in user"""
    return child_obj and isinstance(inspect(current_user).object, Child) and child_obj == current_user


def child_is_my_friend(child_obj):
    """Checks whether child is currently logged in child's friend"""
    return child_obj and isinstance(inspect(current_user).object, Child) and child_obj in current_user.all_friends


def child_is_me_or_my_child(child_obj):
    """
    Checks whether current user is parent and owns child, or current user is that child itself
    :param child_obj: child object we want to check
    :return: whether it is current user, or it's child
    """
    return child_is_me(child_obj) or child_is_my_child(child_obj)


def child_is_me_or_my_child_or_friend(child_obj):
    """
    Same as ``me_or_my_child``, but also allows current user to be a friend of the child.
    :param child_obj: Child object we want to check
    :return: whether current user is the child itself, parent of, or friend of them """
    return child_is_me_or_my_child(child_obj) or child_is_my_friend(child_obj)


def child_is_me_or_my_friend(child_obj):
    """
    Check whether child supplied is currently logged in child, or one if it's friends.
    :param child_obj: a Child object we want to check
    :return: whether the logged in child, or their friend
    """
    return child_is_me(child_obj) or child_is_my_friend(child_obj)


def find_next_time(qst, time_now=datetime.utcnow()):
    """Helper function to find next occurrence of a quest"""
    if not qst.recurring:
        return qst.due
    time = qst.due
    for ts in itertools.cycle([e.value for e in qst.timestamps]):
        if time >= time_now:
            return time
        time = time + timedelta(seconds=ts)


def is_qst_completed(qst, ts):
    """Find if a quest was completed for the certain ts due time"""
    if find_next_time(qst, ts) != ts:
        raise BackboneException(400, "Invalid due time")
    return ts in [c.value for c in qst.completions]


def allowed_file(filename):
    """See if a is acceptable to be uploaded"""
    filename, ext = filename.rsplit('.', 1)
    return ext in ALLOWED_EXTENSIONS


def look_for_file(path, prefix):
    """Check if there's a file that starts with prefix in path, if so return it's absolute path, otherwise None.
    If there's more than one file that start with prefix then it will return one of their absolute paths."""
    try:
        return join(path, filter(lambda e: e.startswith(prefix), listdir(path)).__next__())
    except StopIteration:
        return None


def generate_unique_parent_api_key():
    """Generate unique api key for parents specifically"""
    # noinspection PyTypeChecker
    return generate_unique_api_key_for(Parent)


def generate_unique_child_api_key():
    """Generate unique api key for parents specifically"""
    # noinspection PyTypeChecker
    return generate_unique_api_key_for(Child)


def generate_unique_api_key_for(cls):
    """Helper function, to make unique api_key for cls"""
    new_api_key = str(uuid4())
    # make sure it's a unique api key
    while cls.query.filter_by(api_key=new_api_key).first() is not None:
        new_api_key = str(uuid4())
    return str(new_api_key)


def get_childs_quest_with_window(child, start, lookahead):
    """This helper function generates a list of the descriptions of a child's quests due in the time window."""
    start = datetime.utcfromtimestamp(start)
    end = start + timedelta(seconds=lookahead)

    # Get one time quests in window
    one_time_relevants = [q for q in child.quests if not q.recurring and start <= q.due <= end]

    # Get reoccurring time quests in window
    recurring_relevants = [q for q in child.quests if q.recurring and
                           start <= find_next_time(q, start) <= end]

    return one_time_relevants + recurring_relevants


def _until_next_level(current_level):
    # For now this is a constant 12 xp / level, but this
    #  function makes us able to change this on the fly
    str(current_level)  # suppress warning about unused parameter
    return 12


def award_xp_to_child(child, exp):
    """Helper function to award xp to a child, allows leveling up multiple levels by completing 1 quest.
    Returns whether the child leveled up."""
    child.xp += exp
    to_reach = _until_next_level(child.level)
    if child.xp >= to_reach:
        while child.xp >= to_reach:
            child.level += 1
            child.xp -= to_reach
            to_reach = _until_next_level(child.level)
        return True
    return False
