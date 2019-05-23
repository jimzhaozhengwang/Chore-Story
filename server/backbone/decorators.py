from functools import wraps

from flask import abort
from flask_login import current_user, login_required


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
    """Unified decorator, does the same as login_required + admin_only"""
    @login_required
    @admin_only
    @wraps(func)
    def decorated_view(*args, **kwargs):
        return func(*args, **kwargs)
    return decorated_view
