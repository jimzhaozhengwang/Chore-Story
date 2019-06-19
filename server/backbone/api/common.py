from datetime import datetime

from sqlalchemy import inspect
from werkzeug.utils import secure_filename
from flask import request, current_app as app
import os

from . import *


@api_bp.route('/logout', methods=['POST'])
@login_required
@json_content_only
def logout():
    """
    .. :quickref: User; logout of account

    Destroy ``api_key`` associated with currently logged in user.

    **Login required, either Parent, or Child**

    **Example return**:

    .. code-block:: json

        {
        "data": null
        }

    :return: whether api key has been destroyed
    """
    current_user.api_key = None
    db.session.commit()
    logout_user()
    return current_user.api_key is None


@api_bp.route('/me', methods=['GET'])
@login_required
@backbone_error_handle
def me():
    """
    .. :quickref: User; return a description of logged in user

    Get a description of current user.

    **Login required, either Parent, or Child**

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "children": [1, 2],
            "email": "markooo.keller@gmail.com",
            "id": 1,
            "name": "mark",
            "type": "parent"
          }
        }

    :return: description of currently logged in user
    """
    if isinstance(inspect(current_user).object, Parent):
        resp = {'type': 'parent'}
        resp.update(generate_prnt_resp(current_user))
    else:
        resp = {'type': 'child'}
        resp.update(generate_chd_resp(current_user))
    return json_return(resp)


@api_bp.route('/child/<int:cid>', methods=['GET'])
@login_required
@backbone_error_handle
def get_child_info(cid):
    """
    .. :quickref: User; return a description of a child

    Get info of child with id ``cid`` if they're either the currently logged in parent's child, or
    if it is a friend of the currently logged in child user.

    **Login Required**

    **Errors**:

    404, Child not found - Child either doesn't exist, or permission denied

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "id": 2,
            "level": 1,
            "name": "Jim",
            "xp": 0
          }
        }

    :return: description of currently logged in user
    """
    if isinstance(inspect(current_user).object, Parent):
        child = Child.query.filter_by(id=cid).first()
        if not child or child not in current_user.children:
            raise BackboneException(404, "Child not found")
        return json_return(generate_chd_resp(child))
    elif isinstance(inspect(current_user).object, Child):
        friend = Child.query.filter_by(id=cid).first()
        if not friend or friend not in current_user.all_friends:
            raise BackboneException(404, "Child not found")
        return json_return(generate_chd_resp(friend))


@api_bp.route('/quest/<int:qid>', methods=['GET'], defaults={'ts': None})
@api_bp.route('/quest/<int:qid>/<float:ts>', methods=['GET'])
@login_required
@backbone_error_handle
def get_quest(qid, ts):
    """
    .. :quickref: Quest; get quest description

    Get information about a quest, with next occurrence after ``ts``, if not supplied,
    current time will be used

    **Login required, either Parent, or Child**

    **Errors**:

    404, Quest not found - quest not one of children's, or not own quest of child

    **Example return**:

    .. code-block:: json

        {
         "data": {
           "completed_on": "",
           "description": "You're going on a quest to save the princess, brush your teeth.",
           "due": 1559145600.0,
           "id": 1,
           "next_occurrence": 1559404800.0,
           "recurring": true,
           "reward": 12,
           "title": "This is the initial quest"
          }
        }

    :param qid: id of quest of interest
    :param ts: timestamp we want the ``next_occurrence`` to be relative to if omitted current time will be used
    :return: a JSON object describing the quest, see example
    """
    if not ts:
        ts = datetime.utcnow()
    else:
        ts = datetime.fromtimestamp(ts)
    quest = Quest.query.filter_by(id=qid).first()
    if isinstance(inspect(current_user).object, Parent):
        if quest.owner not in [c.id for c in current_user.children]:
            raise BackboneException(404, "Quest not found")
    elif isinstance(inspect(current_user).object, Child):
        if quest not in current_user.quests:
            raise BackboneException(404, "Quest not found")
    return json_return(generate_qst_resp(quest, ts))


@api_bp.route('/child/<int:cid>', methods=['POST'])
@login_required
@backbone_error_handle
def modify_child(cid, name):
    """
    .. :quickref: Child; modify already existing child

    Search for child and update it's name if current user, or current user's child.

    **Login required, either Parent, or Child**

    **Errors**:

    404, Child not found - child doesn't exists, or permission denied

    **Example post body**:

    .. code-block:: json

        {
          "name": "Jim"
        }

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "id": 2,
            "level": 1,
            "name": "Jim",
            "xp": 0
          }
        }

    :param cid: child's id
    :param name: new name of child
    :return: description of child after update
    """
    child = Child.query.filter_by(id=cid).first()
    if (not child or
            isinstance(inspect(current_user).object, Parent) and child not in current_user.children or
            isinstance(inspect(current_user).object, Child) and child != current_user):
        raise BackboneException(404, "Child not found")
    child.name = name
    db.session.commit()
    return json_return(generate_chd_resp(child))


@api_bp.route('/child/<int:cid>/picture', methods=['POST'])
@login_required
@backbone_error_handle
def upload_child_picture(cid):
    """
    .. :quickref: Child; modify already existing child

    Upload a picture for the currently logged in child, or one of the currently loged in parent's children.

    **Login required, either Parent, or Child**

    **Errors**:

    404, Child not found - child doesn't exists, or permission denied

    :param cid: id of child who's picture we're uploading
    :return: whether the picture has been uploaded sucessfully
    """
    child = Child.query.filter_by(id=cid).first()
    if (not child or
            isinstance(inspect(current_user).object, Parent) and child not in current_user.children or
            isinstance(inspect(current_user).object, Child) and child != current_user):
        raise BackboneException(404, "Child not found")
    if 'file' not in request.files:
        return json_return(False)
    file = request.files['file']
    if file.filename == '':
        return False
    if file and allowed_file(file.filename):
             filename = secure_filename(file.filename)
             new_file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename) 
             file.save(new_file_path)
             return json_return(os.path.isfile(new_file_path))
    
