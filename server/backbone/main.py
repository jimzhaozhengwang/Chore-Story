import click
from flask import Blueprint, render_template
from flask.cli import with_appcontext
from flask_login import login_required, current_user

from backbone import create_app

main = Blueprint('main', __name__)


@main.route('/')
def index():
    return render_template('index.html')


@main.route('/profile')
@login_required
def profile():
    return render_template('profile.html', name=current_user.name)

@click.command("init-db")
@with_appcontext
def init_db_command():
    from backbone import db, create_app
    db.create_all(app=create_app()) # pass the create_app result so Flask-SQLAlchemy gets the configuration.


def init_app(app):
    """Register database functions with the Flask app. This is called by
    the application factory.
    """
    app.cli.add_command(init_db_command)


if __name__ == '__main__':
    create_app().run(host='0.0.0.0', port=4000)
