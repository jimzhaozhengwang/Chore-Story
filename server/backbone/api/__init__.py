import itertools
from datetime import timedelta, datetime

from flask_login import login_required, current_user, logout_user


from .. import db
from ..views import api_bp
from ..models import Parent, Child, Quest, QuestTimes, QuestCompletions
from ..decorators import json_content_only, json_return, backbone_error_handle, parent_login_required, child_login_required
from ..exceptions import BackboneException


def generate_prnt_resp(prnt):
    """
    Generate a dictionary description of a Parent object, used by for example /me
    :param prnt: a Parent object
    :return: a dictionary of description of Parent object
    """
    d = {}
    for e in ['email', 'name', 'id']:
        d[e] = getattr(prnt, e)
    d['children'] = [c.id for c in prnt.children]
    return d


def generate_chd_resp(chd):
    """
    Generate a dictionary description of a Child object, used by for example /me
    :param chd: a Parent object
    :return: a dictionary of description of Parent object
    """
    d = {}
    for e in ['level', 'name', 'id', 'xp']:
        d[e] = getattr(chd, e)
    return d


def generate_qst_resp(qst, ts=datetime.utcnow()):
    """
    Generate a dictionary description of a  object, used by for example /get_quest
    :param qst: a Parent object
    :return: a dictionary of description of Parent object
    """
    d = {}
    for e in ['title', 'description', 'reward', 'due', 'id', 'recurring']:
        d[e] = getattr(qst, e)
    # Add extra values that we don't store in database
    looking_for = find_next_time(qst, ts) if qst.recurring else qst.due
    d['due'] = datetime.timestamp(d['due'])
    if qst.recurring:
        d['next_occurrence'] = datetime.timestamp(looking_for)
    filtered_completions = list(filter(lambda c: c.value == looking_for, qst.completions))
    d['completed_on'] = filtered_completions[0].ts if len(filtered_completions) == 1 else ''
    return d


def find_next_time(qst, time_now=datetime.utcnow()):
    """Helper function to find next occurrence of a recurring event"""
    time = qst.due
    for ts in itertools.cycle([e.value for e in qst.timestamps]):
        if time >= time_now:
            return time
        time = time + timedelta(seconds=ts)


__all__ = ['db', 'Parent', 'Child', 'api_bp', 'generate_prnt_resp', 'generate_chd_resp',
           'login_required', 'current_user', 'logout_user', 'json_content_only', 'BackboneException',
           'parent_login_required', 'child_login_required', 'Quest', 'generate_qst_resp',
           'json_return', 'backbone_error_handle', 'QuestTimes', 'find_next_time', 'QuestCompletions']

from . import child, parent, common
