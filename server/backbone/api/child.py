from . import *


@api_bp.route('/until_next_level', methods=['GET'])
@child_login_required
@json_content_only
def until_next_level(current_level):
    # For now this is a constant 12 xp / level, but this
    #  function makes us able to change this on the fly
    return 12


@api_bp.route('/add_friend', methods=['POST'])
@child_login_required
@json_content_only
def add_friend(fid):
    potential_friend = Child.query.filter_by(id=fid).first()
    if not potential_friend:
        raise BackboneException(404, "Child not found")
    current_user.added_friends.append(potential_friend)
    db.session.commit()


@api_bp.route('/get_friends', methods=['GET'])
@child_login_required
@json_content_only
def get_friends():
    return [f.id for f in current_user.all_friends]