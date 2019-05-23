from flask_login import UserMixin
from sqlalchemy import Table

from . import db

user_table = 'parents'
child_table = 'children'

family_association = Table('parents-children', db.Model.metadata,
                           db.Column('parent_id', db.Integer, db.ForeignKey(f'{user_table}.id')),
                           db.Column('child_id', db.Integer, db.ForeignKey(f'{child_table}.id')))


class Parent(UserMixin, db.Model):
    __tablename__ = user_table
    id = db.Column(db.Integer, nullable=False, primary_key=True)
    admin = db.Column(db.Boolean, nullable=False, default=False)
    name = db.Column(db.String(1000), nullable=False)
    email = db.Column(db.String(100), nullable=False, unique=True)
    password = db.Column(db.String(100), nullable=False)
    children = db.relationship('Child', secondary=family_association)


class Child(db.Model):
    __tablename__ = child_table
    id = db.Column(db.Integer, nullable=False, primary_key=True, autoincrement=True)
    level = db.Column(db.Integer)
    xp = db.Column(db.Integer)
    name = db.Column(db.String(1000), nullable=False)

