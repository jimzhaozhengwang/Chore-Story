from flask_login import UserMixin
from sqlalchemy import Table

from . import db

user_table = 'parents'
child_table = 'children'
quest_table = 'quests'
quest_times_table = 'questTimes'
quest_completions_table = 'questCompletions'

family_association = Table('parents-children', db.Model.metadata,
                           db.Column('parent_id', db.Integer, db.ForeignKey(f'{user_table}.id')),
                           db.Column('child_id', db.Integer, db.ForeignKey(f'{child_table}.id',
                                                                           ondelete='CASCADE',
                                                                           onupdate='CASCADE')))

friendship = Table('friendships', db.Model.metadata,
                   db.Column('friend_a_id', db.Integer, db.ForeignKey(f'{user_table}.id',
                                                                      ondelete='CASCADE',
                                                                      onupdate='CASCADE'), primary_key=True),
                   db.Column('friend_b_id', db.Integer, db.ForeignKey(f'{user_table}.id',
                                                                      ondelete='CASCADE',
                                                                      onupdate='CASCADE'), primary_key=True)
                   )


class Parent(UserMixin, db.Model):
    __tablename__ = user_table

    id = db.Column(db.Integer, nullable=False, primary_key=True)
    admin = db.Column(db.Boolean, nullable=False, default=False)
    api_key = db.Column(db.String(37), default=None, unique=True)
    name = db.Column(db.String(1000), nullable=False)
    email = db.Column(db.String(100), nullable=False, unique=True)
    password = db.Column(db.String(100), nullable=False)
    children = db.relationship('Child', secondary=family_association, backref="parents", cascade="all,delete")

    def __repr__(self):
        return f"Parent {self.name}"


class Child(UserMixin, db.Model):
    __tablename__ = child_table

    id = db.Column(db.Integer, nullable=False, primary_key=True, autoincrement=True)
    api_key = db.Column(db.String(37), default=None, unique=True)
    level = db.Column(db.Integer)
    xp = db.Column(db.Integer)
    name = db.Column(db.String(1000), nullable=False)
    added_friends = db.relationship("Child", secondary=friendship,
                                    primaryjoin=(id == friendship.c.friend_a_id),
                                    secondaryjoin=(id == friendship.c.friend_b_id))
    quests = db.relationship('Quest', cascade="all,delete")

    def __repr__(self):
        return f"Child {self.name}"


class Quest(db.Model):
    __tablename__ = quest_table

    id = db.Column(db.Integer, nullable=False, primary_key=True, autoincrement=True)
    title = db.Column(db.String(350), nullable=False)
    description = db.Column(db.String(1000), nullable=False)
    reward = db.Column(db.Integer, nullable=False)
    owner = db.Column(db.Integer, db.ForeignKey(f'{child_table}.id'))
    due = db.Column(db.TIMESTAMP, nullable=False)
    recurring = db.Column(db.Boolean, default=False, nullable=False)
    timestamps = db.relationship("QuestTimes", cascade="all,delete")
    completions = db.relationship("QuestCompletions", cascade="all,delete")

    def __repr__(self):
        return ("Recurring " if self.recurring else "") + \
               f"Quest {self.id}"


class QuestTimes(db.Model):
    __tablename__ = quest_times_table

    id = db.Column(db.Integer, nullable=False, primary_key=True, autoincrement=True)
    owner = db.Column(db.Integer, db.ForeignKey(f'{quest_table}.id',
                                                ondelete='CASCADE',
                                                onupdate='CASCADE', ))
    value = db.Column(db.Float, nullable=False)


class QuestCompletions(db.Model):
    __tablename__ = quest_completions_table

    id = db.Column(db.Integer, nullable=False, primary_key=True, autoincrement=True)  # ID of completion
    owner = db.Column(db.Integer, db.ForeignKey(f'{quest_table}.id',
                                                ondelete='CASCADE',
                                                onupdate='CASCADE', ))  # Quest's ID
    value = db.Column(db.TIMESTAMP, nullable=False)  # Which due date was completed
    ts = db.Column(db.TIMESTAMP, nullable=False)  # When was it completed


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
