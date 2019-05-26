import inspect
from functools import wraps
from flask import abort, request, json
from flask_login import current_user, login_required

from .models import Parent, Child
from .exceptions import BackboneException


def parent_login_required(func):
    """
    This decorator is the same as login_required, but makes sure logged in user is a of parent type
    :param func: The view function to decorate.
    :type func: function
    """
    @login_required
    @wraps(func)
    def decorated_view(*args, **kwargs):
        if isinstance(current_user, Parent):
            abort(404)
        return func(*args, **kwargs)
    return decorated_view


def child_login_required(func):
    """
    This decorator is the same as login_required, but makes sure logged in user is a of child type
    :param func: The view function to decorate.
    :type func: function
    """
    @login_required
    @wraps(func)
    def decorated_view(*args, **kwargs):
        if isinstance(current_user, Child):
            abort(404)
        return func(*args, **kwargs)
    return decorated_view


def admin_login_required(func):
    """
    Unified decorator, does the same as login_required + admin_only
    :param func: The view function to decorate.
    :type func: function
    """
    @parent_login_required
    @wraps(func)
    def decorated_view(*args, **kwargs):
        if not getattr(current_user, 'admin', False):
            # If not admin user send back 404
            abort(404)
        return func(*args, **kwargs)
    return decorated_view


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


def json_content_only(func):
    """
    Unified decorator, does the same as login_required + admin_only
    :param func: The view function to decorate.
    :type func: function
    """
    @wraps(func)
    def json_view_only():
        argspec = inspect.getfullargspec(func)
        args = extract_params(request.json, argspec[0])
        needs_body = len(args) > 0
        try:
            if not request.is_json:
                raise BackboneException(400, 'Method body must be a valid JSON')
            if needs_body:
                return json.jsonify({'data': func(*args)})
            else:
                return json.jsonify({'data': func()})
        except BackboneException as e:
            return json.jsonify({'error': {'status': e.error_code,
                                           'detail': e.message}})
    return json_view_only
