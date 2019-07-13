from datetime import datetime
from uuid import uuid4

from flask import g
from flask_login import current_user, login_required
from werkzeug.security import check_password_hash, generate_password_hash

from .helpers import generate_qst_resp, generate_chd_resp, generate_unique_parent_api_key, \
    generate_unique_child_api_key, child_is_my_child, find_next_time, award_xp_to_child, get_childs_quest_with_window
from .. import db
from ..decorators import backbone_error_handle, parent_login_required, json_return, json_content_only
from ..exceptions import BackboneException
from ..models import Child, Quest, Parent, QuestTimes, Clan, QuestVerifications
from ..views import api_bp


@api_bp.route('/register', methods=['POST'], defaults={'cp_code': None})
@api_bp.route('/register/<string:cp_code>', methods=['POST'], defaults={'clan_name': None})
@json_content_only
def register(cp_code, email, name, password, clan_name):
    """
    .. :quickref: User; register a parent account

    Register a new account with ``email``, ``name`` and ``password``.
    If ``cp_code`` is sent then corresponding parent's children are copied to new parent.
    Note ``clan_name`` can be not excluded, if there is a ``cp_code``.

    **Errors**:

    409, Email already user - email is already in use by another user
    410, Bad cp_code - invalid co_parent_code

    **Example post body**:

    .. code-block:: json

        {
        "email": "example@inter.net",
        "name": "backbone",
        "password": "backbone",
        "clan_name": "clan_name"
        }

    **Example return**:

    .. code-block:: json

        {
          "data": "1c728327-aad5-42c2-9593-756871542fe2"
        }

    :param email: email address of new user
    :param name: name of new user
    :param password: password of new user
    :param cp_code: co parent code form an already existing parent
    :param clan_name: name of the clan to be added to the database
    :return: an api key for this user
    """
    if Parent.query.filter_by(email=email).first():
        raise BackboneException(409, "Email already used")
    # noinspection PyArgumentList
    new_user = Parent(email=email, name=name, password=generate_password_hash(password), clan_id=clan_name)
    if cp_code:
        other_parent = Parent.query.filter_by(cp_code=cp_code).first()
        if not other_parent:
            raise BackboneException(410, "Can't find co parent")
        new_user.clan_id = other_parent.clan_id
        other_parent.co_parent_code = None
    else:
        new_clan = Clan(name=clan_name)
        db.session.add(new_clan)
        db.session.commit()
        new_user.clan_id = new_clan.id
    db.session.add(new_user)
    new_user.api_key = generate_unique_parent_api_key()
    db.session.commit()
    return new_user.api_key


@api_bp.route('/login', methods=['POST'])
@json_content_only
def login(email, password):
    """
    .. :quickref: User; login a parent account

    Creates an ``api_key`` for user specified in post body. If there
    was one previously then it replaces it.

    **Errors**:

    406, Bad login details - user with ``email`` and/or ``password`` not found

    **Example post body**:

    .. code-block:: json

        {
        "email": "example@inter.net",
        "password": "backbone"
        }

    **Example return**:

    .. code-block:: json

        {
        "data": "aa7fae90-d9b0-48c3-83d8-acfb663914bb"
        }

    :param email: email address of user
    :param password: password of user
    :return: new api_key of user
    """
    user = Parent.query.filter_by(email=email).first()

    if not user or not check_password_hash(user.password, password):
        raise BackboneException(406, "Bad login details")

    # even though we did not authenticate by a header, let's skip adding cookie to the response
    g.login_via_request = True
    user.api_key = generate_unique_parent_api_key()
    db.session.commit()
    return user.api_key


@api_bp.route('/child', methods=['POST'])
@parent_login_required
@backbone_error_handle
def add_child():
    """
    .. :quickref: Child; add a child account

    Creates a new child account associated with current parent account.
    Note: only one of these can be registered at a time.

    **Parent login required**

    **Example return**:

    .. code-block:: json

        {
          "data": "1fbb1906-c79c-4db4-a701-a059440ab543"
        }

    :return: a description of the new child
    """
    new_ch_code = str(uuid4())
    # make sure it's a unique cp_code
    while Parent.query.filter_by(ch_code=new_ch_code).first() is not None:
        new_ch_code = str(uuid4())
    current_user.ch_code = str(new_ch_code)
    db.session.commit()
    return json_return(new_ch_code)


@api_bp.route('/child/<int:cid>/delete', methods=['POST'])
@login_required
@backbone_error_handle
def delete_child(cid):
    """
    .. :quickref: Child; delete existing child

    Search for child and delete them if current user's child.

    **Parent login required**

    **Errors**:

    404, Child not found - child doesn't exists, or permission denied

    **Example return**:

    .. code-block:: json

        {
          "data": true
        }

    :param cid: child's id
    :return: whether child was deleted
    """
    child = Child.query.filter_by(id=cid).first()
    if not child_is_my_child(child):
        raise BackboneException(404, "Child not found")
    db.session.delete(child)
    db.session.commit()
    return json_return(Child.query.filter_by(id=cid).first() is None)


@api_bp.route('/child/<int:cid>/login', methods=['GET'])
@parent_login_required
@backbone_error_handle
def generate_child_login(cid):
    """
    .. :quickref: User; generate child login token

    Generates an api key for a child of the currently logged in Parent user.
    If there was an api key associated with child this will replace it.

    **Parent login required**

    **Errors**:

    404, Child not found - user has no access to child with id ``cid``, or
    child doesn't exist.

    **Example return**:

    .. code-block:: json

        {
        "data": "f927c8b2-1a58-4d59-b8ac-7c7259d960e7"
        }

    :param cid: id of child
    :return: new api_key of child
    """
    child = Child.query.filter_by(id=cid).first()
    if not child_is_my_child(child):
        raise BackboneException(404, "Child not found")
    child.api_key = generate_unique_child_api_key()
    db.session.commit()
    return json_return(child.api_key)


@api_bp.route('/child/<int:cid>/quest', methods=['POST'])
@parent_login_required
@json_content_only
def add_quest(cid, title, description, reward, due, needs_verification, timestamps=None):
    """
    .. :quickref: Quest; add a quest to an own child

    Add a quest to one of the current user's children.

    **Parent login required**

    **Example post body**:

    .. code-block:: json

        {
         "title": "Brush your teeth",
         "description": "You're going on a quest to save the princess, brush your teeth.",
         "reward": 2,
         "timestamps": [86400],
         "due": 1559131200,
         "needs_verification": true
        }

    **Errors**:

    404, Child not found - user has no access to child with id ``cid``, or
    child doesn't exist.

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "completed_now": false,
            "lvled_up": false,
            "qst": {
              "completed_on": "Sat, 06 Jul 2019 21:11:35 GMT",
              "description": "You're going on a quest to save the princess, brush your teeth so you don't embarrass yourself.",
              "due": 1559250000.0,
              "id": 1,
              "needs_verification": true,
              "recurring": false,
              "reward": 12,
              "title": "This is the initial quest",
              "verified_on": null
            }
          }
        }

    :param cid: id of child
    :param title: title of quest
    :param description: description of quest
    :param reward: number of exp points to reward child for completing quest
    :param due: first, or only completion timestamp
    :param needs_verification: whether this quest needs verification
    :param timestamps: a list of timestamps, for quest to be done by those intervals
    :return: description of the new quest
    """
    child = Child.query.filter_by(id=cid).first()
    if not child_is_my_child(child):
        raise BackboneException(404, "Child not found")
    due = datetime.utcfromtimestamp(int(due))
    timestamps = [QuestTimes(value=ts) for ts in timestamps]
    new_quest = Quest(title=title,
                      description=description,
                      reward=reward,
                      due=due,
                      recurring=len(timestamps) != 0,
                      timestamps=timestamps,
                      needs_verification=needs_verification)
    child.quests.append(new_quest)
    for ts in timestamps:
        db.session.add(ts)
    db.session.add(new_quest)
    db.session.commit()
    return generate_qst_resp(new_quest)


@api_bp.route('/quest/<int:qid>', methods=['POST'])
@parent_login_required
@json_content_only
def modify_quest(qid, title, description, reward, due, timestamps, needs_verification):
    """
    .. :quickref: User; modify a quest of an own child

    Modify a quest belonging to an own child.

    **Parent login required**

    **Example post body**:

    .. code-block:: json

        {
         "cid": 1,
         "title": "Brush your teeth",
         "description": "You're going on a quest to save the princess, brush your teeth.",
         "reward": 2,
         "needs_verification": false,
         "timestamps": [86400],
         "due": 1559131200
        }

    **Errors**:

    404, Child not found - user has no access to child with id ``cid``, or
    child doesn't exist.

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "completed_now": false,
            "lvled_up": false,
            "qst": {
              "completed_on": "Sat, 06 Jul 2019 21:11:35 GMT",
              "description": "You're going on a quest to save the princess, brush your teeth so you don't embarrass yourself.",
              "due": 1559250000.0,
              "id": 1,
              "needs_verification": true,
              "recurring": false,
              "reward": 12,
              "title": "This is the initial quest",
              "verified_on": null
            }
          }
        }

    :param qid: id of quest
    :param title: title of quest
    :param description: description of quest
    :param reward: number of exp points to reward child for completing quest
    :param due: first, or only completion timestamp
    :param needs_verification: whether this quest needs verification
    :param timestamps: a list of timestamps, for quest to be done by those intervals
    :return: description of the updated quest
    """
    old_quest = Quest.query.filter_by(id=qid).first()
    if not old_quest:
        raise BackboneException(404, "Quest not found")
    child = Child.query.filter_by(id=old_quest.owner).first()
    if not child_is_my_child(child):
        raise BackboneException(404, "Child not found")
    old_quest.title = title
    old_quest.description = description
    old_quest.reward = reward
    old_quest.due = datetime.utcfromtimestamp(due)
    old_quest.needs_verification = needs_verification
    for ots in old_quest.timestamps:
        db.session.delete(ots)
    old_quest.timestamps = [QuestTimes(value=ts) for ts in timestamps]
    db.session.commit()
    return generate_qst_resp(old_quest)


@api_bp.route('/quest/<int:qid>/delete', methods=['POST'])
@parent_login_required
@backbone_error_handle
def delete_quest(qid):
    """
    .. :quickref: Quest; delete a quest

    Delete a quest belonging to one of the current user's children.

    **Parent login required**

    **Errors**:

    404, Quest not found - either quest doesn't exist, or permission denied

    **Example return**:

    .. code-block:: json

        {
          "data": true
        }

    :param qid: id of quest to be deleted
    :return: whether quest was deleted
    """
    quest = Quest.query.filter_by(id=qid).first()
    if not quest:
        raise BackboneException(404, "Quest not found")
    owner = Child.query.filter_by(id=quest.owner).first()
    if not owner or owner not in current_user.children:
        raise BackboneException(404, "Quest not found")
    db.session.delete(quest)
    db.session.commit()
    return json_return(Quest.query.filter_by(id=qid).first() is None)


@api_bp.route('/parent', methods=['POST'])
@parent_login_required
@backbone_error_handle
def add_co_parent():
    """
    .. :quickref: User; generate co-parent registration code

    **Parent login required**

    **Example return**:

    .. code-block:: json

        {
          "data": "1256d5ab-5cad-4c03-b2b5-5e8163de6092"
        }

    :return: new co parent registration code to be used in the ``/api/register`` end-point.
    """
    new_cp_code = str(uuid4())
    # make sure it's a unique cp_code
    while Parent.query.filter_by(cp_code=new_cp_code).first() is not None:
        new_cp_code = str(uuid4())
    current_user.cp_code = str(new_cp_code)
    db.session.commit()
    return json_return(new_cp_code)


@api_bp.route('/clan', methods=['POST'])
@parent_login_required
@json_content_only
def modify_clan(name):
    """
    .. :quickref: Clan; modify clan of logged in user

    Modify current user's clan.

    **Login required, either Parent, or Child**

    **Example post body**:

    .. code-block:: json

        {
         "name": "new name"
        }

    **Example return**:

    .. code-block:: json

        {
          "data": true
        }

    :return: Whether clan name was modified.
    """
    current_user.clan.name = name
    db.session.commit()
    return current_user.clan.name == name


@api_bp.route('/child/<int:cid>/quest', methods=['GET'])
@parent_login_required
@backbone_error_handle
def get_child_quests(cid):
    """
    .. :quickref: Quest; get all quests of a child

    Returns the list of a child's quests.

    **Parent login required**

    **Example return**:

    .. code-block:: json

        {
          "data": [
            {
              "completed_on": null,
              "description": "You're going on a quest to save the princess, brush your teeth so you don't embarrass yourself.",
              "due": 1559250000.0,
              "id": 1,
              "needs_verification": true,
              "recurring": false,
              "reward": 99,
              "title": "This is the initial quest",
              "verified_on": null
            }
          ]
        }

    :return: Whether clan name was modified.
    """
    child = Child.query.filter_by(id=cid).first()
    if not child_is_my_child(child):
        raise BackboneException(404, "Child not found")
    return json_return([generate_qst_resp(q) for q in child.quests])


@api_bp.route('/child/<int:cid>/quest/<float:start>', methods=['GET'], defaults={'lookahead': 86400.0})
@api_bp.route('/child/<int:cid>/quest/<float:start>/<float:lookahead>', methods=['GET'])
@parent_login_required
@backbone_error_handle
def get_child_quests_window(cid, start, lookahead):
    """
    .. :quickref: Quest; get a child's quests within look around window

    Get ids of quests due in the range [start, start + 24 hrs], if ``ts`` is not supplied, current time is used.

    **Parent login required**

    **Example Return**:

    .. code-block:: json

        {
          "data": [
            {
              "completed_on": null,
              "description": "You're going on a quest to save the princess, brush your teeth so you don't embarrass yourself.",
              "due": 1559250000.0,
              "id": 1,
              "needs_verification": true,
              "recurring": false,
              "reward": 99,
              "title": "This is the initial quest",
              "verified_on": null
            }
          ]
        }

    :param cid: id of child
    :param start: timestamp that should be used to determine window start/end
    :param lookahead: timestamp that should be used to determine size of the window
    :return: list of quest ids
    """
    child = Child.query.filter_by(id=cid).first()
    if not child_is_my_child(child):
        raise BackboneException(404, "Child not found")
    quests = get_childs_quest_with_window(start, lookahead)
    return json_return([generate_qst_resp(q) for q in quests])


@api_bp.route('/quest/<int:qid>/verify', methods=['POST'], defaults={'ts': None})
@api_bp.route('/quest/<int:qid>/<float:ts>/verify', methods=['POST'])
@parent_login_required
@backbone_error_handle
def verify_quest_completion(qid, ts):
    """
    .. :quickref: quest; verify a quest completion

    Verify a quest that has been completed.

    **Parent login required**

    **errors**:

    404, quest not found - quest does not exists, or not currently logged in child's

    **example return**:

    .. code-block:: json

        {
          "data": {
            "lvled_up": true,
            "qst": {
              "completed_on": "Sat, 06 Jul 2019 21:30:53 GMT",
              "description": "You're going on a quest to save the princess, brush your teeth so you don't embarrass yourself.",
              "due": 1559250000.0,
              "id": 2,
              "needs_verification": true,
              "recurring": false,
              "reward": 12,
              "title": "This is the initial quest",
              "verified_on": "Sat, 06 Jul 2019 21:34:36 GMT"
            },
            "verified_now": true
          }
        }

    :param qid: id of quest to be completed
    :param ts: timestamp the quest needs to be completed by
    :return: quest description with ``lvled_up`` and ``completed_now`` filds added
    """
    quest = Quest.query.filter_by(id=qid).first()
    quest_owner = Child.query.filter_by(id=quest.owner).first()
    if not child_is_my_child(quest_owner):
        raise BackboneException(404, "Quest not found")
    if not ts:
        ts = find_next_time(quest)
    else:
        ts = datetime.fromtimestamp(ts)
    previous_verification = QuestVerifications.query.filter_by(id=qid, value=ts).first()
    verified_now = previous_verification is None
    lvl_up = False
    if verified_now:
        # Not already verified
        quest_verification = QuestVerifications(value=ts, ts=datetime.utcnow())
        quest.verifications.append(quest_verification)
        lvl_up = award_xp_to_child(quest_owner, quest.reward)
        db.session.commit()
    resp = {
        'verified_now': verified_now,
        'lvled_up': lvl_up,
        'qst': generate_qst_resp(quest, ts)
    }
    return json_return(resp)
