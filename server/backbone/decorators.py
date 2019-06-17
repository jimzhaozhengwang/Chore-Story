import inspect
from functools import wraps

from flask import abort, request, json
from flask_login import current_user, login_required

from .exceptions import BackboneException
from .models import Parent, Child


def json_return(output):
    """Helper for returning a json output"""
    return json.jsonify({"data": output})


def backbone_error_handle(func):
    """A decorator for catching BackboneExceptions and returning error messages to clients"""

    @wraps(func)
    def protected_view(*args, **kwargs):
        try:
            return func(*args, **kwargs)
        except BackboneException as e:
            abort(e.error_code)

    return protected_view


def parent_login_required(func):
    """This decorator is the same as login_required, but makes sure logged in user is a of parent type"""

    @login_required
    @wraps(func)
    def decorated_view(*args, **kwargs):
        if isinstance(current_user, Parent):
            abort(404)
        return func(*args, **kwargs)

    return decorated_view


def child_login_required(func):
    """This decorator is the same as login_required, but makes sure logged in user is a of child type"""

    @login_required
    @wraps(func)
    def decorated_view(*args, **kwargs):
        if isinstance(current_user, Child):
            abort(404)
        return func(*args, **kwargs)

    return decorated_view


def admin_login_required(func):
    """Makes sure that user is logged in and that they are of parent type and an admin"""

    @parent_login_required
    @wraps(func)
    def decorated_view(*args, **kwargs):
        if not getattr(current_user, 'admin', False):
            # If not admin user send back 404
            abort(404)
        return func(*args, **kwargs)

    return decorated_view


def extract_params(body, params):
    """Extract params from body nicely if possible"""
    d = {}
    for p in params:
        if p in body.keys():
            d[p] = body[p]
    return d


def json_content_only(func):
    """A decorator to automatically extract json elements"""

    @backbone_error_handle
    @wraps(func)
    def json_view_only(**kwargs):
        args = inspect.getfullargspec(func)[0]
        kwargs.update(extract_params(request.json, args))
        if list(kwargs.keys()) != args:
            raise BackboneException(400, "Invalid input")
        if not request.is_json:
            raise BackboneException(400, 'Method body must be a valid JSON')
        return json_return(func(**kwargs))

    return json_view_only
