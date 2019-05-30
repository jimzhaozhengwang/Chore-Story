from datetime import datetime
from uuid import uuid4

from flask import g
from werkzeug.security import check_password_hash, generate_password_hash

from . import *


@api_bp.route('/register', methods=['POST'])
@json_content_only
def register(email, name, password):
    """
    .. :quickref: User; register a parent account

    Register a new account with ``email``, ``name`` and ``password``.

    **Errors**:

    409, Email already user - email is already in use by another user

    **Example post body**:

    .. code-block:: json

        {
        "email": "example@inter.net",
        "name": "backbone",
        "password": "backbone"
        }

    **Example return**:

    .. code-block:: json

        {
        "data": null
        }

    :param email: email address of new user
    :param name: name of new user
    :param password: password of new user
    :return: None
    """
    user = Parent.query.filter_by(
        email=email).first()  # if this returns a user, then the email already exists in database

    if user:  # if a user is found, we want to redirect back to signup page so user can try again
        raise BackboneException(409, "Email already used")

    # create new user with the form data. Hash the password so plaintext version isn't saved.
    new_user = Parent(email=email, name=name, password=generate_password_hash(password))

    # add the new user to the database
    db.session.add(new_user)
    db.session.commit()
    return None


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

    # check if user actually exists
    # take the user supplied password, hash it, and compare it to the hashed password in database
    if not user or not check_password_hash(user.password, password):
        raise BackboneException(406, "Bad login details")

    # even though we did not authenticate by a header, let's skip adding cookie to the response
    g.login_via_request = True
    new_api_key = str(uuid4())
    # make sure it's a unique api key
    while (Parent.query.filter_by(api_key=new_api_key).first() is not None or
           Child.query.filter_by(api_key=new_api_key).first() is not None):
        new_api_key = str(uuid4())
    user.api_key = str(new_api_key)
    db.session.commit()
    return new_api_key


@api_bp.route('/child', methods=['POST'])
@parent_login_required
@json_content_only
def add_child(name):
    """
    .. :quickref: Child; add a child account

    Creates a new child account associated with current parent account.

    **Parent login required**

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
          "data": {
            "id": 2,
            "level": 1,
            "name": "Jim",
            "xp": 0
          }
        }

    :param email: email address of user
    :param password: password of user
    :return: new api_key of user
    """
    # Generate child
    new_child = Child(level=1, xp=0, name=name)
    current_user.children.append(new_child)
    db.session.add(new_child)
    db.session.commit()
    return generate_chd_resp(new_child)


@api_bp.route('/child_login/<int:cid>', methods=['GET'])
@parent_login_required
@json_content_only
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
    user = current_user
    if child not in user.children:
        raise BackboneException(404, "Child not found")
    new_api_key = str(uuid4())
    # make sure it's a unique api key
    while (Parent.query.filter_by(api_key=new_api_key).first() is not None or
           Child.query.filter_by(api_key=new_api_key).first() is not None):
        new_api_key = str(uuid4())
    child.api_key = new_api_key
    db.session.commit()
    return new_api_key


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
         "description": "You're going on a quest to save the princess, brush your teeth so you don't embarass yourself.",
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
            "description": "You're going on a quest to save the princess, brush your teeth so you don't embarass yourself.",
            "due": 1559131200.0,
            "id": 10,
            "next_occurence": 1559131200.0,
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
def modify_quest(qid, title, description, reward, due, timestamps=None):
    """
    .. :quickref: User; modify a quest of an own child

    Modify a quest belonging to an own child.

    **Parent login required**

    **Example post body**:

    .. code-block:: json

        {
         "cid": 1,
         "title": "Brush your teeth",
         "description": "You're going on a quest to save the princess, brush your teeth so you don't embarass yourself.",
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
            "description": "You're going on a quest to save the princess, brush your teeth so you don't embarass yourself.",
            "due": 1559131200.0,
            "id": 10,
            "next_occurence": 1559131200.0,
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
    :return: description of the updated quest
    """
    # TODO if quest has been done refuse to update
    old_quest = Quest.query.filter_by(id=qid).first()
    if not old_quest:
        raise BackboneException(404, "Child not found")
    child = Child.query.filter_by(id=old_quest.owner).first()
    if child not in current_user.children:
        raise BackboneException(404, "Child not found")
    old_quest.title = title
    old_quest.description = description
    old_quest.reward = reward
    old_quest.due = due
    # TODO if timestamps change old ones should be deleted
    old_quest.timestamps = timestamps
    db.session.commit()
    return generate_qst_resp(old_quest)
