from datetime import datetime, timedelta

from . import *


def _until_next_level(current_level):
    # For now this is a constant 12 xp / level, but this
    #  function makes us able to change this on the fly
    return 12


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


@api_bp.route('/friend/<int:fid>', methods=['POST'])
@child_login_required
@json_content_only
def add_friend(fid):
    """
    .. :quickref: Friend; add a friend

    Add a friend to currently logged in child account.

    **Child login required**

    **Errors**:

    404, Child not found - child id is invalid

    **Example Return**:

    .. code-block:: json

        {
        "data": null
        }

    :param fid: child id of friend
    :return: whether friend was added
    """
    potential_friend = Child.query.filter_by(id=fid).first()
    if not potential_friend:
        raise BackboneException(404, "Child not found")
    current_user.added_friends.append(potential_friend)
    db.session.commit()
    return potential_friend in current_user.all_friends


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

    **Errors**:

    404, Quest not found - quest does not exists, or not currently logged in child's

    **Example Return**:

    .. code-block:: json

        {
         "data": {
          "completed_now": true,
          "lvled_up": false,
          "quest": {
            "completed_on": "1559145134",
            "description": "You're going on a quest to save the princess, brush your teeth so you don't embarass yourself.",
            "due": 1559145600.0,
            "id": 1,
            "next_occurrence": 1559404800.0,
            "recurring": true,
            "reward": 12,
            "title": "Brush your teeth",
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
        db.session.add(quest_completion)
        current_user.xp += quest.reward
        to_reach = _until_next_level(current_user.level)
        if current_user.xp >= to_reach:
            lvl_up = True
            current_user.level += 1
            current_user.xp -= to_reach
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
    return json_return([q.id for q in current_user.quests])


@api_bp.route('/quest/<float:ts>', methods=['GET'], defaults={'lookahead': 86400.0})
@api_bp.route('/quest/<float:ts>/<float:lookahead>', methods=['GET'])
@child_login_required
@backbone_error_handle
def get_quests(ts, lookahead):
    """
    .. :quickref: Quest; get quests within look around window from ts

    Get ids of quests due in the range [ts, ts + 24 hrs], if ``ts`` is not supplied, current time is used.

    **Child login required**

    **Example Return**:

    .. code-block:: json

        {
          "data": [
            1,
            2
          ]
        }

    :param ts: timestamp that should be used to determine window start/end
    :return: list of quest ids
    """
    # Get timestamp range
    start = datetime.utcfromtimestamp(ts)
    end = start + timedelta(seconds=lookahead)

    # Get one time quests in window
    one_time_relevants = [q.id for q in current_user.quests if not q.recurring and start <= q.due <= end]

    # Get reoccurring time quests in window
    recurring_relevants = [q.id for q in current_user.quests if q.recurring and
                           start <= find_next_time(q, start) <= end]

    return json_return(one_time_relevants + recurring_relevants)
