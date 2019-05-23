from flask_login import UserMixin
from sqlalchemy.orm import relationship

from . import db


class User(UserMixin, db.Model):
    __tablename__ = 'users'
    id = db.Column(db.Integer, nullable=False, primary_key=True)  # primary keys are required by SQLAlchemy
    email = db.Column(db.String(100), nullable=False, unique=True)
    password = db.Column(db.String(100), nullable=False)
    name = db.Column(db.String(1000), nullable=False)
    admin = db.Column(db.Boolean, nullable=False, default=False)
    pid = db.Column(db.Integer, nullable=True, default=None)  # connecting account to a parent
    # children = relationship('Avatar')


class Avatar(db.Model):
    __tablename__ = 'avatars'
    uid = db.Column(db.Integer, primary_key=True)  # connecting an avatar to a user
    level = db.Column(db.Integer)
    xp = db.Column(db.Integer)
    child_acc = db.Column(db.Boolean)

