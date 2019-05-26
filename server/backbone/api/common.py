from . import *
from sqlalchemy import inspect


@api_bp.route('/logout', methods=['POST'])
@login_required
@json_content_only
def logout(body):
    current_user.api_key = None
    db.session.commit()
    logout_user()
    return {}


@api_bp.route('/me', methods=['GET'])
@login_required
@json_content_only
def me():
    if isinstance(inspect(current_user).object, Parent):
        return generate_prnt_resp(current_user)
    elif isinstance(inspect(current_user).object, Child):
        return generate_chd_resp(current_user)

