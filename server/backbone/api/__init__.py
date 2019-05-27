from flask import Blueprint
from flask_login import login_required, current_user, logout_user

api_bp = Blueprint('api', __name__)

from .. import db
from ..models import Parent, Child, Quest
from ..decorators import json_content_only, parent_login_required, child_login_required
from ..exceptions import BackboneException


def generate_prnt_resp(usr):
    """
    Generate a dictionary description of a Parent object, used by for example /me
    :param usr: a Parent object
    :return: a dictionary of description of Parent object
    """
    d = {}
    for e in ['email', 'name', 'id']:
        d[e] = getattr(usr, e)
    d['children'] = [c.id for c in usr.children]
    return d


def generate_chd_resp(usr):
    """
    Generate a dictionary description of a Child object, used by for example /me
    :param usr: a Parent object
    :return: a dictionary of description of Parent object
    """
    d = {}
    for e in ['level', 'name', 'id', 'xp']:
        d[e] = getattr(usr, e)
    return d


def generate_qst_resp(usr):
    """
    Generate a dictionary description of a  object, used by for example /get_quest
    :param usr: a Parent object
    :return: a dictionary of description of Parent object
    """
    d = {}
    for e in ['title', 'description', 'reward', 'completed']:
        d[e] = getattr(usr, e)
    return d


__all__ = ['db', 'Parent', 'Child', 'api_bp', 'generate_prnt_resp', 'generate_chd_resp',
           'login_required', 'current_user', 'logout_user', 'json_content_only', 'BackboneException',
           'parent_login_required', 'child_login_required', 'Quest', 'generate_qst_resp']

from . import child, parent, common
