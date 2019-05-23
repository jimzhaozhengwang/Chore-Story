# For strictly api calls, everything here should return nothing but a json
from flask import jsonify, Blueprint, request, abort
from flask_login import login_required, current_user

from . import db
from .models import Child

api_bp = Blueprint('api', __name__)

# Helper functions


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

# Login needed endpoints


@api_bp.route('/me', methods=['GET'])
@login_required
def api_me():
    return jsonify(generate_usr_resp(current_user))


@api_bp.route('/add_child', methods=['POST'])
@login_required
def add_child():
    name = request.form.get('name')
    new_child = Child(level=1, xp=0, name=name)
    current_user.children.append(new_child)
    db.session.add(new_child)
    db.session.commit()
    return jsonify(generate_usr_resp(current_user))


@api_bp.route('/get_child', methods=['POST'])
@login_required
def get_child_info():
    child = Child.query.filter_by(id=request.form.get('id')).first()
    if not child or child not in current_user.children:
        abort(401)
    return jsonify(generate_chd_resp(child))

# Public API endpoints


@api_bp.route('/until_next_level')
def until_next_level():
    current_level = request.form.get('current_level')
    # For now this is a constant 12 xp / level, but this
    #  function makes us able to change this on the fly
    return jsonify({'exp': 12})
