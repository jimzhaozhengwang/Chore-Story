from . import *
from sqlalchemy import inspect
from flask import abort


@api_bp.route('/logout', methods=['POST'])
@login_required
@json_content_only
def logout():
    current_user.api_key = None
    db.session.commit()
    logout_user()
    return {}


@api_bp.route('/me', methods=['GET'])
@login_required
@json_content_only
def me():
    if isinstance(inspect(current_user).object, Parent):
        resp = {'type': 'parent'}
        resp.update(generate_prnt_resp(current_user))
        return resp
    elif isinstance(inspect(current_user).object, Child):
        resp = {'type': 'child'}
        resp.update(generate_chd_resp(current_user))
        return resp


@api_bp.route('/get_child/<int:cid>', methods=['GET'])
@login_required
@backbone_error_handle
def get_child_info(cid):
    if isinstance(inspect(current_user).object, Parent):
        child = Child.query.filter_by(id=cid).first()
        if not child or child not in current_user.children:
            abort(401)
        return generate_chd_resp(child)
    elif isinstance(inspect(current_user).object, Child):
        friend = Child.query.filter_by(id=cid).first()
        if not friend or friend not in current_user.all_friends:
            abort(401)
        return generate_chd_resp(friend)


@api_bp.route('/get_quest/<int:qid>', methods=['GET'])
@login_required
@backbone_error_handle
def get_quest(qid):
    quest = Quest.query.filter_by(id=qid).first()
    if isinstance(inspect(current_user).object, Parent):
        if quest.owner not in [c.id for c in current_user.children]:
            raise BackboneException(404, "Quest not found")
    elif isinstance(inspect(current_user).object, Child):
        if quest not in current_user.quests:
            raise BackboneException(404, "Quest not found")
    return json_return(generate_qst_resp(quest))
