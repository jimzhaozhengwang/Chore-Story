from datetime import datetime
from uuid import uuid4

from flask import g
from flask_login import current_user, login_required
from werkzeug.security import check_password_hash, generate_password_hash

from .helpers import generate_qst_resp, generate_chd_resp, generate_unique_parent_api_key, generate_unique_child_api_key
from .. import db
from ..decorators import backbone_error_handle, parent_login_required, json_return, json_content_only
from ..exceptions import BackboneException
from ..models import Child, Quest, Parent, QuestTimes, Clan
from ..views import api_bp


@api_bp.route('/register', methods=['POST'], defaults={'cp_code': None})
@api_bp.route('/register/<cp_code>', methods=['POST'], defaults={'clan_name': None})
@json_content_only
def register(cp_code, email, name, password, clan_name):
    """
    .. :quickref: User; register a parent account

    Register a new account with ``email``, ``name`` and ``password``.
    If ``cp_code`` is sent then corresponding parent's children are copied to new parent.
    Note ``clan_name`` can be not excluded, if there is a ``cp_code``.

    **Errors**:

    409, Email already user - email is already in use by another user
    410, Bad cp_code - invalid co_parent_code

    **Example post body**:

    .. code-block:: json

        {
        "email": "example@inter.net",
        "name": "backbone",
        "password": "backbone",
        "clan_name": "clan_name"
        }

    **Example return**:

    .. code-block:: json

        {
          "data": "1c728327-aad5-42c2-9593-756871542fe2"
        }

    :param email: email address of new user
    :param name: name of new user
    :param password: password of new user
    :param cp_code: co parent code form an already existing parent
    :param clan_name: name of the clan to be added to the database
    :return: an api key for this user
    """
    if Parent.query.filter_by(email=email).first():
        raise BackboneException(409, "Email already used")
    # noinspection PyArgumentList
    new_user = Parent(email=email, name=name, password=generate_password_hash(password), clan_id=clan_name)
    if cp_code:
        other_parent = Parent.query.filter_by(cp_code=cp_code).first()
        if not other_parent:
            raise BackboneException(410, "Can't find co parent")
        new_user.children = other_parent.children
        new_user.clan_id = other_parent.clan_id
        other_parent.co_parent_code = None
    else:
        new_clan = Clan(name=clan_name)
        db.session.add(new_clan)
        db.session.commit()
        new_user.clan_id = new_clan.id
    db.session.add(new_user)
    new_user.api_key = generate_unique_parent_api_key()
    db.session.commit()
    return new_user.api_key


@api_bp.route('/login', methods=['POST'])
@json_content_only
def login(email, password):
    """
    .. :quickref: User; login a parent account

    Creates an ``api_key`` for user specified in post body. If there
    was one previously then it replaces it.

    **Errors**:

    406, Bad login details - user with ``email`` and/or ``password`` not found

    **Example post body**:

    .. code-block:: json

        {
        "email": "example@inter.net",
        "password": "backbone"
        }

    **Example return**:

    .. code-block:: json

        {
        "data": "aa7fae90-d9b0-48c3-83d8-acfb663914bb"
        }

    :param email: email address of user
    :param password: password of user
    :return: new api_key of user
    """
    user = Parent.query.filter_by(email=email).first()

    if not user or not check_password_hash(user.password, password):
        raise BackboneException(406, "Bad login details")

    # even though we did not authenticate by a header, let's skip adding cookie to the response
    g.login_via_request = True
    user.api_key = generate_unique_parent_api_key()
    db.session.commit()
    return user.api_key


@api_bp.route('/child', methods=['POST'])
@parent_login_required
@json_content_only
def add_child(name):
    """
    .. :quickref: Child; add a child account

    Creates a new child account associated with current parent account.

    **Parent login required**

    **Example post body**:

    .. code-block:: json

        {
        "name": "Jim",
        }

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "clan_name": "Marky Mark",
            "id": 1,
            "level": 69,
            "name": "Amanda",
            "xp": 0
          }
        }

    :param name: name of child
    :return: a description of the new child
    """
    # noinspection PyArgumentList
    new_child = Child(level=1, xp=0, name=name)
    current_user.children.append(new_child)
    db.session.add(new_child)
    db.session.commit()
    return generate_chd_resp(new_child)


@api_bp.route('/child/<int:cid>/delete', methods=['POST'])
@login_required
@backbone_error_handle
def delete_child(cid):
    """
    .. :quickref: Child; delete existing child

    Search for child and delete them if current user's child.

    **Parent login required**

    **Errors**:

    404, Child not found - child doesn't exists, or permission denied

    **Example return**:

    .. code-block:: json

        {
          "data": true
        }

    :param cid: child's id
    :return: whether child was deleted
    """
    child = Child.query.filter_by(id=cid).first()
    if not child or child not in current_user.children:
        raise BackboneException(404, "Child not found")
    db.session.delete(child)
    db.session.commit()
    return json_return(Child.query.filter_by(id=cid).first() is None)


@api_bp.route('/child_login/<int:cid>', methods=['GET'])
@parent_login_required
@backbone_error_handle
def generate_child_login(cid):
    """
    .. :quickref: User; generate child login token

    Generates an api key for a child of the currently logged in Parent user.
    If there was an api key associated with child this will replace it.

    **Parent login required**

    **Errors**:

    404, Child not found - user has no access to child with id ``cid``, or
    child doesn't exist.

    **Example return**:

    .. code-block:: json

        {
        "data": "f927c8b2-1a58-4d59-b8ac-7c7259d960e7"
        }

    :param cid: id of child
    :return: new api_key of child
    """
    child = Child.query.filter_by(id=cid).first()
    if child not in current_user.children:
        raise BackboneException(404, "Child not found")
    child.api_key = generate_unique_child_api_key()
    db.session.commit()
    return json_return(child.api_key)


@api_bp.route('/quest', methods=['POST'])
@parent_login_required
@json_content_only
def add_quest(cid, title, description, reward, due, timestamps=None):
    """
    .. :quickref: Quest; add a quest to an own child

    Add a quest to one of the current user's children.

    **Parent login required**

    **Example post body**:

    .. code-block:: json

        {
         "cid": 1,
         "title": "Brush your teeth",
         "description": "You're going on a quest to save the princess, brush your teeth.",
         "reward": 2,
         "timestamps": [86400],
         "due": 1559131200
        }

    **Errors**:

    404, Child not found - user has no access to child with id ``cid``, or
    child doesn't exist.

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "completed_on": "",
            "description": "You're going on a quest to save the princess, brush your teeth.",
            "due": 1559131200.0,
            "id": 10,
            "next_occurrence": 1559131200.0,
            "recurring": true,
            "reward": 2,
            "title": "Brush your teeth"
          }
        }

    :param cid: id of child
    :param title: title of quest
    :param description: description of quest
    :param reward: number of exp points to reward child for completing quest
    :param due: first, or only completion timestamp
    :param timestamps: a list of timestamps, for quest to be done by those intervals
    :return: description of the new quest
    """
    child = Child.query.filter_by(id=cid).first()
    if child not in current_user.children:
        raise BackboneException(404, "Child not found")
    due = datetime.utcfromtimestamp(int(due))
    timestamps = [QuestTimes(value=ts) for ts in timestamps]
    new_quest = Quest(title=title,
                      description=description,
                      reward=reward,
                      due=due,
                      recurring=len(timestamps) != 0,
                      timestamps=timestamps)
    child.quests.append(new_quest)
    for ts in timestamps:
        db.session.add(ts)
    db.session.add(new_quest)
    db.session.commit()
    return generate_qst_resp(new_quest)


@api_bp.route('/quest/<int:qid>', methods=['POST'])
@parent_login_required
@json_content_only
def modify_quest(qid, title, description, reward, due, timestamps):
    """
    .. :quickref: User; modify a quest of an own child

    Modify a quest belonging to an own child.

    **Parent login required**

    **Example post body**:

    .. code-block:: json

        {
         "cid": 1,
         "title": "Brush your teeth",
         "description": "You're going on a quest to save the princess, brush your teeth.",
         "reward": 2,
         "timestamps": [86400],
         "due": 1559131200
        }

    **Errors**:

    404, Child not found - user has no access to child with id ``cid``, or
    child doesn't exist.

    **Example return**:

    .. code-block:: json

        {
          "data": {
            "completed_on": "",
            "description": "You're going on a quest to save the princess, brush your teeth.",
            "due": 1559131200.0,
            "id": 10,
            "next_occurrence": 1559131200.0,
            "recurring": true,
            "reward": 2,
            "title": "Brush your teeth"
          }
        }

    :param qid: id of quest
    :param title: title of quest
    :param description: description of quest
    :param reward: number of exp points to reward child for completing quest
    :param due: first, or only completion timestamp
    :param timestamps: a list of timestamps, for quest to be done by those intervals
    :return: description of the updated quest
    """
    old_quest = Quest.query.filter_by(id=qid).first()
    if not old_quest:
        raise BackboneException(404, "Quest not found")
    child = Child.query.filter_by(id=old_quest.owner).first()
    if child not in current_user.children:
        raise BackboneException(404, "Child not found")
    old_quest.title = title
    old_quest.description = description
    old_quest.reward = reward
    old_quest.due = datetime.utcfromtimestamp(due)
    for ots in old_quest.timestamps:
        db.session.delete(ots)
    old_quest.timestamps = [QuestTimes(value=ts) for ts in timestamps]
    db.session.commit()
    return generate_qst_resp(old_quest)


@api_bp.route('/quest/<int:qid>/delete', methods=['POST'])
@parent_login_required
@backbone_error_handle
def delete_quest(qid):
    """
    .. :quickref: Quest; delete a quest

    Delete a quest belonging to one of the current user's children.

    **Parent login required**

    **Errors**:

    404, Quest not found - either quest doesn't exist, or permission denied

    **Example return**:

    .. code-block:: json

        {
          "data": true
        }

    :param qid: id of quest to be deleted
    :return: whether quest was deleted
    """
    quest = Quest.query.filter_by(id=qid).first()
    if not quest:
        raise BackboneException(404, "Quest not found")
    owner = Child.query.filter_by(id=quest.owner).first()
    if not owner or owner not in current_user.children:
        raise BackboneException(404, "Quest not found")
    db.session.delete(quest)
    db.session.commit()
    return json_return(Quest.query.filter_by(id=qid).first() is None)


@api_bp.route('/generate_cp_code', methods=['POST'])
@parent_login_required
@backbone_error_handle
def add_co_parent():
    """
    .. :quickref: User; generate co-parent registration code

    **Parent login required**

    **Example return**:

    .. code-block:: json

        {
          "data": "1256d5ab-5cad-4c03-b2b5-5e8163de6092"
        }

    :return: new co parent registration code to be used in the ``/api/register`` end-point.
    """
    # even though we did not authenticate by a header, let's skip adding cookie to the response
    g.login_via_request = True
    new_cp_code = str(uuid4())
    # make sure it's a unique cp_code
    while Parent.query.filter_by(cp_code=new_cp_code).first() is not None:
        new_cp_code = str(uuid4())
    current_user.cp_code = str(new_cp_code)
    db.session.commit()
    return json_return(new_cp_code)
