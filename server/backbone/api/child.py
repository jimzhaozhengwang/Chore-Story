from . import *


def _until_next_level(current_level):
    # For now this is a constant 12 xp / level, but this
    #  function makes us able to change this on the fly
    return 12

@api_bp.route('/until_next_level/<int:current_level>', methods=['GET'])
@child_login_required
@backbone_error_handle
def until_next_level(current_level):
    """Returns how much xp is needed to level up to current_level+1"""
    return _until_next_level(current_level)


@api_bp.route('/add_friend', methods=['POST'])
@child_login_required
@json_content_only
def add_friend(fid):
    """Add child of id fid"""
    potential_friend = Child.query.filter_by(id=fid).first()
    if not potential_friend:
        raise BackboneException(404, "Child not found")
    current_user.added_friends.append(potential_friend)
    db.session.commit()
    # TODO what to return here
    return True


@api_bp.route('/get_friends', methods=['GET'])
@child_login_required
@json_content_only
def get_friends():
    return [f.id for f in current_user.all_friends]


@api_bp.route('/complete_quest', methods=['POST'])
@child_login_required
@json_content_only
def complete_quest(qid):
    """returns whether child lvled up"""
    quest = Quest.query.filter_by(id=qid).first()
    if quest not in current_user.quests:
        raise BackboneException(404, "Quest not found")
    if quest.completed:
        return False
    # Update quest and child objects
    quest.completed = True
    current_user.xp += quest.reward
    to_reach = _until_next_level()
    lvl_up = False
    if current_user.xp >= to_reach:
        lvl_up = True
        current_user.level += 1
        current_user.xp -= to_reach
    db.session.commit()
    return lvl_up


@api_bp.route('/get_quests', methods=['GET'])
@child_login_required
@json_content_only
def get_quests():
    return [q.id for q in current_user.quests]
