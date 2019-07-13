from datetime import datetime, timedelta

from flask import g
from flask_login import current_user

from .helpers import find_next_time, is_qst_completed, generate_qst_resp, award_xp_to_child, _until_next_level, \
    get_childs_quest_with_window, generate_unique_child_api_key
from .. import db
from ..decorators import child_login_required, backbone_error_handle, json_return, json_content_only
from ..exceptions import BackboneException
from ..models import Child, Quest, QuestCompletions, Parent
from ..views import api_bp


@api_bp.route('/lvlup/<int:current_level>', methods=['GET'])
@child_login_required
@backbone_error_handle
def until_next_level(current_level):
    """
    .. :quickref: User; get exp limit of level

    Returns how much xp is needed to level up to ``current_level`` + 1

    **Child login required**

    **Example return**:

    .. code-block:: json

        {
        "data": 12
        }

    :param current_level: the level from which we'd like to level up
    :return: return the number of xp needed to level up.
    """
    return json_return(_until_next_level(current_level))


@api_bp.route('/friend/<username>', methods=['POST'])
@child_login_required
@json_content_only
def add_friend(username):
    """
    .. :quickref: Friend; add a friend

    Add a friend to currently logged in child account based on their username.

    **Child login required**

    **Errors**:

    404, Child not found - child id is invalid

    **Example Return**:

    .. code-block:: json

        {
        "data": true
        }

    :param username: user name of child
    :return: whether friend was added
    """
    potential_friend = Child.query.filter_by(username=username).first()
    if not potential_friend:
        raise BackboneException(404, "Child not found")
    current_user.added_friends.append(potential_friend)
    db.session.commit()
    return json_return(potential_friend in current_user.all_friends)


@api_bp.route('/friend', methods=['GET'])
@child_login_required
@backbone_error_handle
def get_friends():
    """
    .. :quickref: Friend; list ids of friend

    List ids of currently logged in user.

    **Child login required**

    **Example Return**:

    .. code-block:: json

        {
         "data": [
            1,
            2
         ]
        }

    :return: list of child ids
    """
    return json_return([f.id for f in current_user.all_friends])


@api_bp.route('/quest/<int:qid>/complete', methods=['POST'], defaults={'ts': None})
@api_bp.route('/quest/<int:qid>/<float:ts>/complete', methods=['POST'])
@child_login_required
@backbone_error_handle
def complete_quest(qid, ts):
    """
    .. :quickref: Quest; complete a quest

    Complete a quest that has completion at ``ts``, if ``ts`` not supplied, then the closest/only occurrence
    will be completed.

    **Child login required**

    **errors**:

    404, quest not found - quest does not exists, or not currently logged in child's

    **example return**:

    .. code-block:: json

        {
          "data": {
            "completed_now": true,
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

    :param qid: id of quest to be completed
    :param ts: timestamp the quest needs to be completed by
    :return: quest description with ``lvled_up`` and ``completed_now`` filds added
    """
    quest = Quest.query.filter_by(id=qid).first()
    if quest not in current_user.quests:
        raise BackboneException(404, "Quest not found")
    if not ts:
        ts = find_next_time(quest)
    else:
        ts = datetime.fromtimestamp(ts)
    completed_now = not is_qst_completed(quest, ts)
    lvl_up = False
    if completed_now:
        quest_completion = QuestCompletions(value=ts, ts=datetime.utcnow())
        quest.completions.append(quest_completion)
        if not quest.needs_verification:
            lvl_up = award_xp_to_child(current_user, quest.award)
        db.session.commit()
    resp = {
        'completed_now': completed_now,
        'lvled_up': lvl_up,
        'qst': generate_qst_resp(quest, ts)
    }
    return json_return(resp)


@api_bp.route('/quest', methods=['GET'])
@child_login_required
@backbone_error_handle
def get_all_quests():
    """
    .. :quickref: Quest; get all quests of current child

    Get all of the currently logged in child's quest ids

    **Child login required**

    **Example Return**:

    .. code-block:: json

        {
         "data": [
          1,
          2
         ]
        }

    :return: list of the quests ids of current child
    """
    return json_return([generate_qst_resp(q) for q in current_user.quests])


@api_bp.route('/quest/<float:start>', methods=['GET'], defaults={'lookahead': 86400.0})
@api_bp.route('/quest/<float:start>/<float:lookahead>', methods=['GET'])
@child_login_required
@backbone_error_handle
def get_quests(start, lookahead):
    """
    .. :quickref: Quest; get quests within look around window from ts

    Get ids of quests due in the range [ts, ts + 24 hrs], if ``ts`` is not supplied, current time is used.

    **Child login required**

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

    :param start: timestamp that should be used to determine window start/end
    :param lookahead: timestamp that should be used to determine size of the window
    :return: list of quest ids
    """
    quests = get_childs_quest_with_window(start, lookahead)
    return json_return([generate_qst_resp(q) for q in quests])


@api_bp.route('/child/<string:ch_code>', methods=['POST'])
@json_content_only
def child_register(ch_code, name, username):
    """
    .. :quickref: Child; register a child account

    Registers a new child account associated with parent account who's child code is supplied..

    **Errors**:

    409, Username is already taken - username is already in use by another child
    404, Invalid ch_code - invalid ch_code

    **Example post body**:

    .. code-block:: json

        {
        "name": "Jim",
        "username": "beastmaster69"
        }

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "clan_name": "Marky Mark",
            "id": 1,
            "level": 42,
            "name": "Jim",
            "username": "beastmaster69",
            "xp": 0
          }
        }

    :param name: name of child
    :param username: username of child
    :return: a description of the new child
    """
    # even though we did not authenticate by a header, let's skip adding cookie to the response
    g.login_via_request = True
    parent = Parent.query.filter_by(ch_code=ch_code).first()
    if not parent:
        raise BackboneException(404, "Invalid ch_code")
    conflict_username = Child.query.filter_by(username=username).first()
    if conflict_username:
        raise BackboneException(409, "Username is already taken")
    # noinspection PyArgumentList
    new_child = Child(level=1, xp=0, name=name, username=username, clan_id=parent.clan_id)
    new_child.api_key = generate_unique_child_api_key()
    db.session.add(new_child)
    db.session.commit()
    return new_child.api_key
