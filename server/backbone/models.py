from flask_login import UserMixin
from sqlalchemy import Table

from . import db

user_table = 'parents'
child_table = 'children'
quest_table = 'quests'

family_association = Table('parents-children', db.Model.metadata,
                           db.Column('parent_id', db.Integer, db.ForeignKey(f'{user_table}.id')),
                           db.Column('child_id', db.Integer, db.ForeignKey(f'{child_table}.id')))

# TODO test this once child api is up
friendship = Table('friendships', db.Model.metadata,
                   db.Column('friend_a_id', db.Integer, db.ForeignKey(f'{user_table}.id'), primary_key=True),
                   db.Column('friend_b_id', db.Integer, db.ForeignKey(f'{user_table}.id'), primary_key=True)
                   )


class Parent(UserMixin, db.Model):
    __tablename__ = user_table

    id = db.Column(db.Integer, nullable=False, primary_key=True)
    admin = db.Column(db.Boolean, nullable=False, default=False)
    api_key = db.Column(db.String(36), default=None, unique=True)
    name = db.Column(db.String(1000), nullable=False)
    email = db.Column(db.String(100), nullable=False, unique=True)
    password = db.Column(db.String(100), nullable=False)
    children = db.relationship('Child', secondary=family_association)

    def __repr__(self):
        return f"Parent {self.name}"


class Child(UserMixin, db.Model):
    __tablename__ = child_table

    id = db.Column(db.Integer, nullable=False, primary_key=True, autoincrement=True)
    api_key = db.Column(db.String(36), default=None, unique=True)
    level = db.Column(db.Integer)
    xp = db.Column(db.Integer)
    name = db.Column(db.String(1000), nullable=False)
    added_friends = db.relationship("Child", secondary=friendship,
                                    primaryjoin=(id == friendship.c.friend_a_id),
                                    secondaryjoin=(id == friendship.c.friend_b_id))
    quests = db.relationship('Quest')

    def __repr__(self):
        return f"Child {self.name}"


class Quest(db.Model):
    __tablename__ = quest_table

    id = db.Column(db.Integer, nullable=False, primary_key=True, autoincrement=True)
    title = db.Column(db.String(350), nullable=False)
    description = db.Column(db.String(1000), nullable=False)
    reward = db.Column(db.Integer, nullable=False)
    completed = db.Column(db.Boolean, default=False, nullable=False)

    def __repr__(self):
        return f"Quest {self.name}"


friendship_union = db.select([
    friendship.c.friend_a_id,
    friendship.c.friend_b_id
]).union(
    db.select([
        friendship.c.friend_b_id,
        friendship.c.friend_a_id]
    )
).alias()

Child.all_friends = db.relationship('Child',
                                    secondary=friendship_union,
                                    primaryjoin=Child.id == friendship_union.c.friend_a_id,
                                    secondaryjoin=Child.id == friendship_union.c.friend_b_id,
                                    viewonly=True)
