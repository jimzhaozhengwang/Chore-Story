from functools import wraps
from flask import abort, request, json
from flask_login import current_user, login_required

from .exceptions import BackboneException


def admin_only(func):
    """
    This decorator will restrict access of endpoint to admins only
    :param func: The view function to decorate.
    :type func: function
    """
    @wraps(func)
    def decorated_view(*args, **kwargs):
        if not getattr(current_user, 'admin', False):
            # If not admin user send back 404
            abort(404)
        return func(*args, **kwargs)
    return decorated_view


def admin_login_required(func):
    """
    Unified decorator, does the same as login_required + admin_only
    :param func: The view function to decorate.
    :type func: function
    """
    @login_required
    @admin_only
    @wraps(func)
    def decorated_view(*args, **kwargs):
        return func(*args, **kwargs)
    return decorated_view


def json_content_only(func):
    """
    Unified decorator, does the same as login_required + admin_only
    :param func: The view function to decorate.
    :type func: function
    """
    @wraps(func)
    def json_view_only():
        try:
            if not request.is_json:
                raise BackboneException(400, 'Method body must be a valid JSON')
            return json.jsonify({'data': func(request.json)})
        except BackboneException as e:
            return json.jsonify({'error': {'status': e.error_code,
                                           'detail': e.message}})
    return json_view_only
