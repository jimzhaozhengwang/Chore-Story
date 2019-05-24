# For strictly api calls, everything here should return nothing but a json
from uuid import uuid4

from flask import Blueprint, abort, flash, url_for, redirect, g
from flask_login import login_required, current_user
from werkzeug.security import check_password_hash

from .exceptions import BackboneException
from .decorators import json_content_only
from . import db
from .models import Child, Parent

api_bp = Blueprint('api', __name__)

# Helper functions


def extract_params(body, params):
    """
    Extract params from body nicely
    :param params: list of param names
    :param body: body in a dictionary form
    :return: a tuple of the extracted params
    """
    try:
        return tuple(body[e] for e in params)
    except KeyError:
        raise BackboneException(400, "Invalid post body")


def generate_usr_resp(usr):
    d = {}
    for e in ['email', 'name', 'id']:
        d[e] = getattr(usr, e)
    d['children'] = [c.id for c in usr.children]
    return d


def generate_chd_resp(usr):
    d = {}
    for e in ['level', 'name', 'id', 'xp']:
        d[e] = getattr(usr, e)
    return d

# Login/out endpoints


@api_bp.route('/login', methods=['POST'])
@json_content_only
def login(body):
    email, password = extract_params(body, ['email', 'password'])

    user = Parent.query.filter_by(email=email).first()

    # check if user actually exists
    # take the user supplied password, hash it, and compare it to the hashed password in database
    if not user or not check_password_hash(user.password, password):
        flash('Please check your login details and try again.')
        return redirect(url_for('auth.login'))  # if user doesn't exist or password is wrong, reload the page

    # even though we did not authenticate by a header, let's skip adding cookie to the response
    g.login_via_request = True
    new_api_key = uuid4()
    user.api_key = str(new_api_key)
    db.session.commit()
    return {'api_key': new_api_key}


@api_bp.route('/logout', methods=['POST'])
@login_required
@json_content_only
def logout(body):
    current_user.api_key = None
    db.session.commit()
    return {}

# Login needed endpoints


@api_bp.route('/me', methods=['GET'])
@login_required
@json_content_only
def me(body):
    return generate_usr_resp(current_user)


@api_bp.route('/add_child', methods=['POST'])
@login_required
@json_content_only
def add_child(body):
    # Get params
    name, = extract_params(body, ['name'])
    # Generate child
    new_child = Child(level=1, xp=0, name=name)
    current_user.children.append(new_child)
    db.session.add(new_child)
    db.session.commit()
    return generate_chd_resp(new_child)


@api_bp.route('/get_child', methods=['POST'])
@login_required
@json_content_only
def get_child_info(body):
    # Get params
    cid, = extract_params(body, ['id'])
    # Return info
    child = Child.query.filter_by(id=cid).first()
    if not child or child not in current_user.children:
        abort(401)
    return generate_chd_resp(child)

# Public API endpoints


@api_bp.route('/until_next_level')
@json_content_only
def until_next_level(body):
    current_level = body['current_level']
    # For now this is a constant 12 xp / level, but this
    #  function makes us able to change this on the fly
    return {'exp': 12}
