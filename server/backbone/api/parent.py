from os import abort
from uuid import uuid4

from flask import g
from werkzeug.security import check_password_hash

from . import *


@api_bp.route('/login', methods=['POST'])
@json_content_only
def login(email, password):
    user = Parent.query.filter_by(email=email).first()

    # check if user actually exists
    # take the user supplied password, hash it, and compare it to the hashed password in database
    if not user or not check_password_hash(user.password, password):
        raise BackboneException(406, "Bad login details")

    # even though we did not authenticate by a header, let's skip adding cookie to the response
    g.login_via_request = True
    new_api_key = str(uuid4())
    # make sure it's a unique api key
    while (Parent.query.filter_by(api_key=new_api_key).first() is not None or
           Child.query.filter_by(api_key=new_api_key).first() is not None):
        new_api_key = str(uuid4())
    user.api_key = str(new_api_key)
    db.session.commit()
    return {'api_key': new_api_key}


@api_bp.route('/add_child', methods=['POST'])
@parent_login_required
@json_content_only
def add_child(name):
    # Generate child
    new_child = Child(level=1, xp=0, name=name)
    current_user.children.append(new_child)
    db.session.add(new_child)
    db.session.commit()
    return generate_chd_resp(new_child)


@api_bp.route('/generate_child_login')
@parent_login_required
@json_content_only
def generate_child_login(cid):
    child = Child.query.filter_by(id=cid).first()
    user = current_user
    if child not in user.children:
        raise BackboneException(403, "Not user's child")
    new_api_key = str(uuid4())
    # make sure it's a unique api key
    while (Parent.query.filter_by(api_key=new_api_key).first() is not None or
           Child.query.filter_by(api_key=new_api_key).first() is not None):
        new_api_key = str(uuid4())
    child.api_key = new_api_key
    db.session.commit()
    return {'api_key': new_api_key}


@api_bp.route('/get_child', methods=['POST'])
@parent_login_required
@json_content_only
def get_child_info(cid):
    child = Child.query.filter_by(id=cid).first()
    if not child or child not in current_user.children:
        abort(401)
    return generate_chd_resp(child)

