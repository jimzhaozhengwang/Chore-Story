from datetime import datetime

import os
from flask import request, current_app as app, send_file
from flask_login import current_user, logout_user
from sqlalchemy import inspect
from werkzeug.utils import secure_filename

from .helpers import child_is_me_or_my_child, generate_chd_resp, generate_qst_resp, generate_prnt_resp, allowed_file, \
    child_is_me_or_my_child_or_friend, look_for_file, generate_clan_resp
from .. import db
from ..decorators import json_content_only, json_return, login_required, backbone_error_handle
from ..exceptions import BackboneException
from ..models import Child, Parent, Quest
from ..views import api_bp


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
            "children": [
              {
                "id": 1,
                "name": "child"
              }
            ],
            "clan_name": "makyy mark",
            "email": "markooo.keller@gmail.com",
            "id": 1,
            "name": "Mark",
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
    if it is a friend of the currently logged in child user, or the logged in child itself.

    **Login Required**

    **Errors**:

    404, Child not found - Child either doesn't exist, or permission denied

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "clan_name": "marky mark",
            "id": 1,
            "level": 1,
            "name": "child",
            "username": "c123"
            "parents": [
              {
                "id": 1,
                "name": "Mark"
              }
            ],
            "xp": 0
          }
        }

    :return: description of currently logged in user
    """
    child = Child.query.filter_by(id=cid).first()
    if not child_is_me_or_my_child_or_friend(child):
        raise BackboneException(404, "Child not found")
    return json_return(generate_chd_resp(child))


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
    if not child_is_me_or_my_child(Child.query.filter_by(id=quest.owner).first()):
        raise BackboneException(404, "Quest not found")
    return json_return(generate_qst_resp(quest, ts))


@api_bp.route('/child/<int:cid>', methods=['POST'])
@login_required
@backbone_error_handle
def modify_child(cid, name, username):
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
            "clan_name": "makyy mark",
            "id": 1,
            "level": 1,
            "name": "Jim",
            "parents": [
              {
                "id": 1,
                "name": "Mark"
              }
            ],
            "xp": 0
          }
        }

    :param cid: child's id
    :param name: new name of child
    :return: description of child after update
    """
    child = Child.query.filter_by(id=cid).first()
    if not child_is_me_or_my_child(child):
        raise BackboneException(404, "Child not found")
    child.name = name
    child.username = username
    db.session.commit()
    return json_return(generate_chd_resp(child))


@api_bp.route('/child/<int:cid>/picture', methods=['POST'])
@login_required
@backbone_error_handle
def upload_child_picture(cid):
    """
    .. :quickref: Child; upload picture for child

    Upload a picture for the currently logged in child, or one of the currently loged in parent's children.
    If there was a picture previously uploaded, this will replace it.

    **Login required, either Parent, or Child**

    **Errors**:

    404, Child not found - child doesn't exists, or permission denied

    :param cid: id of child who's picture we're uploading
    :return: whether the picture has been uploaded successful
    """
    child = Child.query.filter_by(id=cid).first()
    if not child_is_me_or_my_child(child):
        raise BackboneException(404, "Child not found")
    file = request.files.get('file')
    if file and file.filename != '' and allowed_file(file.filename):
        looking_in = os.path.join(app.config['UPLOAD_FOLDER'], 'child_pics')
        # Delete previous picture if exists
        previous_file = look_for_file(looking_in, str(cid) + '.')
        if previous_file:
            os.remove(previous_file)
        # Save new picture
        _, ext = secure_filename(file.filename).rsplit('.', 1)
        new_file_path = os.path.join(looking_in, str(cid) + '.' + ext)
        file.save(new_file_path)
        return json_return(os.path.isfile(new_file_path))
    return json_return(False)


@api_bp.route('/child/<int:cid>/picture', methods=['GET'])
@login_required
@backbone_error_handle
def show_child_picture(cid):
    """
    .. :quickref: Child; return already existing child picture

    Return the picture of a child in one of 3 cases: if the currently logged in child requests their own picture, if the
    parent request it's child's picture, or if a friend of the child requests their photo.

    **Login required, either Parent, or Child**

    **Errors**:

    404, Child not found - child doesn't exists, or permission denied
    405, Picture not found - child doesn't have a picture

    :param cid: id of child who's picture we're looking for
    :return: the picture itself if they exist
    """
    child = Child.query.filter_by(id=cid).first()
    if not child_is_me_or_my_child_or_friend(child):
        raise BackboneException(404, "Child not found")
    file = look_for_file(os.path.join(app.config['UPLOAD_FOLDER'], 'child_pics'), str(cid) + '.')
    if not file:
        raise BackboneException(405, "Picture not found")
    return send_file(file)


@api_bp.route('/clan', methods=['GET'])
@login_required
@backbone_error_handle
def get_clan():
    """
    .. :quickref: Clan; return a description of logged in user's clan

    Get a description of current user's clan.

    **Login required, either Parent, or Child**

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "children": [
              {
                "id": 1,
                "name": "child"
              }
            ],
            "id": 1,
            "name": "marky mark",
            "parents": [
              {
                "id": 1,
                "name": "Mark"
              }
            ]
          }
        }

    :return: description of currently logged in user's clan
    """
    return json_return(generate_clan_resp(current_user.clan))


@api_bp.route('/clan/children', methods=['GET'])
@login_required
@backbone_error_handle
def get_clan_children():
    """
    .. :quickref: Clan; return a list of clan's children descriptions

    Get a  description of current user's clan children.

    **Login required, either Parent, or Child**

    **Example return**:

    .. code-block:: json

        {
          "data": [
            {
              "clan_name": "Marky Mark",
              "id": 1,
              "level": 1,
              "name": "Amanda",
              "parents": [
                {
                  "id": 1,
                  "name": "Mark"
                }
              ],
              "username": "mandy",
              "xp": 0
            }
          ]
        }

    :return: description of currently logged in user's clan children
    """
    return json_return([generate_chd_resp(c) for c in current_user.clan.children])


@api_bp.route('/me/registration_id', methods=['POST'])
@login_required
@json_content_only
def save_registration_number(registration_id):
    """
    .. :quickref: User; save registration id

    Save firebase registration number for notifications.

    **Login required, either Parent, or Child**

    **Example return**:

    .. code-block:: json

        {
          "data": {
            true
          }
        }

    :return: whether the saved registration id was changes succesfully
    """
    current_user.registration_id = registration_id
    db.session.commit()
    return current_user.registration_id == registration_id


@api_bp.route('/quest/<int:qid>/verification', methods=['POST'])
@login_required
@backbone_error_handle
def upload_quest_verification_picture(qid):
    """
    .. :quickref: Quest; upload picture for verification

    Upload a picture for quest for verification purposes.
    If there was a picture previously uploaded, this will replace it.

    **Login required, either Parent, or Child**

    **Errors**:

    404, Quest not found - child doesn't exists, or permission denied

    :param qid: id of quest who's verification picture we're uploading
    :return: whether the picture has been uploaded successful
    """
    quest = Quest.query.filter_by(id=qid).first()
    if not quest:
        raise BackboneException(404, "Quest not found")
    file = request.files.get('file')
    if file and file.filename != '' and allowed_file(file.filename):
        looking_in = os.path.join(app.config['UPLOAD_FOLDER'], 'verification_pics')
        # Delete previous picture if exists
        previous_file = look_for_file(looking_in, str(qid) + '.')
        if previous_file:
            os.remove(previous_file)
        # Save new picture
        _, ext = secure_filename(file.filename).rsplit('.', 1)
        new_file_path = os.path.join(looking_in, str(qid) + '.' + ext)
        file.save(new_file_path)
        return json_return(os.path.isfile(new_file_path))
    return json_return(False)


@api_bp.route('/quest/<int:qid>/verification', methods=['GET'])
@login_required
@backbone_error_handle
def show_verification_picture(qid):
    """
    .. :quickref: Quest; return already existingt verification picture

    Return the picture of a quest in 2 cases: if the currently logged in child requests their own verification picture,
    if the parent request it's child's verification picture.

    **Login required, either Parent, or Child**

    **Errors**:

    404, Quest not found - owner child doesn't exists, or permission denied
    405, Picture not found - quest doesn't have a verification picture

    :param cid: id of child who's picture we're looking for
    :return: the picture itself if they exist
    """
    quest = Quest.query.filter_by(id=qid).first()
    if not child_is_me_or_my_child_or_friend(quest.owner):
        raise BackboneException(404, "Quest not found")
    file = look_for_file(os.path.join(app.config['UPLOAD_FOLDER'], 'verification_pics'), str(qid) + '.')
    if not file:
        raise BackboneException(405, "Picture not found")
    return send_file(file)
