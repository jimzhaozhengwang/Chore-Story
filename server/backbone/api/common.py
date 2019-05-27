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


@api_bp.route('/get_child', methods=['GET'])
@login_required
@json_content_only
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
