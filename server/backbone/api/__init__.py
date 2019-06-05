import itertools
from datetime import timedelta, datetime

from flask_login import login_required, current_user, logout_user

from .. import db
from ..decorators import json_content_only, json_return, backbone_error_handle, parent_login_required, \
    child_login_required
from ..exceptions import BackboneException
from ..models import Parent, Child, Quest, QuestTimes, QuestCompletions
from ..views import api_bp


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
    :param chd: a Child object
    :return: a dictionary of description of Child object
    """
    d = {}
    for e in ['level', 'name', 'id', 'xp']:
        d[e] = getattr(chd, e)
    return d


def generate_qst_resp(qst, ts=datetime.utcnow()):
    """
    Generate a dictionary description of a Quest object, used by for example /get_quest
    :param qst: a Quest object
    :param ts: a datetime object that we want to get next occurrence from
    :return: a dictionary of description of Quest object
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
    d['completed_on'] = filtered_completions[0].ts if len(filtered_completions) == 1 else None
    return d


def find_next_time(qst, time_now=datetime.utcnow()):
    """Helper function to find next occurrence of a quest"""
    if not qst.recurring:
        return qst.due
    time = qst.due
    for ts in itertools.cycle([e.value for e in qst.timestamps]):
        if time >= time_now:
            return time
        time = time + timedelta(seconds=ts)


def is_qst_completed(qst, ts):
    """find if a quest was completed for the certain ts due time"""
    if find_next_time(qst, ts) != ts:
        raise BackboneException(400, "Invalid due time")
    return ts in [c.value for c in qst.completions]


__all__ = ['db', 'Parent', 'Child', 'api_bp', 'generate_prnt_resp', 'generate_chd_resp',
           'login_required', 'current_user', 'logout_user', 'json_content_only', 'BackboneException',
           'parent_login_required', 'child_login_required', 'Quest', 'generate_qst_resp',
           'json_return', 'backbone_error_handle', 'QuestTimes', 'find_next_time', 'QuestCompletions',
           'is_qst_completed']

from . import child, parent, common
