import click
from flask import Flask, g
from flask.sessions import SecureCookieSessionInterface
from flask_login import LoginManager, user_loaded_from_request

from .db import db
from . import api


def grab_file_content(name, required):
    import os
    import sys
    path = os.path.join(os.path.dirname(__file__), name)
    if not os.path.exists(path):
        if required:
            print(f"{name} file is missing, we need this file", file=sys.stderr)
            exit(200)
        else:
            return None
    try:
        with open(path, 'r') as f:
            return f.read().strip()
    except (IOError, FileNotFoundError):
        if required:
            print(f"{name} file could not be read, we need this file", file=sys.stderr)
            exit(200)
        else:
            return None


def grab_secret_key():
    return grab_file_content('secret_key.txt', True)


def grab_project_id():
    return grab_file_content('project_id.txt', False)

def grab_notif_api_key():
    return grab_file_content('notif_api_key.txt', False)


def create_app():
    app = Flask(__name__)

    project_id = grab_project_id()
    if project_id:
        app.config['DIALOGFLOW_PROJECT_ID'] = project_id
    notif_api_key = grab_notif_api_key()
    if notif_api_key:
        app.config['NOTIF_API_KEY'] = notif_api_key
    app.config['SECRET_KEY'] = grab_secret_key()
    app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///db.sqlite'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024  # 16 MB
    app.config['UPLOAD_FOLDER'] = '/backbone/uploads'

    db.init_app(app)

    login_manager = LoginManager()
    login_manager.login_view = 'auth.login'
    login_manager.init_app(app)

    from .models import Parent, Child

    @login_manager.user_loader
    def load_user(user_id):
        # since the user_id is just the primary key of our user table, use it in the query for the user
        return Parent.query.get(int(user_id))

    @login_manager.request_loader
    def load_user_from_request(request):
        api_key = request.headers.get('Authorization')
        if api_key:
            # TODO turn this back on
            # try:
            #     api_key = base64.b64decode(api_key)
            # except TypeError:
            #     pass
            try:
                parent_key, child_key = api_key.split(':', 1)
            except ValueError:
                return
            if ((parent_key != '' and child_key != '') or  # someone trying to hack?
                    (parent_key == '' and child_key == '')):  # is a developer sending api key?
                return
            user = (Parent.query.filter_by(api_key=parent_key).first() if parent_key else
                    Child.query.filter_by(api_key=child_key).first())
            return user
        return

    # blueprint for auth routes in our app
    from .auth import auth as auth_blueprint
    app.register_blueprint(auth_blueprint)

    # blueprint for api calls
    from .views import api_bp
    app.register_blueprint(api_bp, url_prefix='/api')

    # noinspection PyUnusedLocal
    @user_loaded_from_request.connect
    def user_loaded_from_header(self, user=None):
        g.login_via_request = True

    # blueprint for non-auth parts of app
    from .main import main as main_blueprint
    app.register_blueprint(main_blueprint)

    @app.cli.command('init-db')
    def init_db_command():
        """Clear existing data and create new tables."""
        import os
        db_file = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'db.sqlite')
        if os.path.isfile(db_file):
            print(f"Removing already existing db file: {db_file}")
            os.remove(db_file)
        db.create_all(app=app)
        click.echo("Initialized the database.")

    class CustomSessionInterface(SecureCookieSessionInterface):
        """Prevent creating session from API requests."""

        def save_session(self, *args, **kwargs):
            if g.get('login_via_request'):
                return
            return super(CustomSessionInterface, self).save_session(*args,
                                                                    **kwargs)

    app.session_interface = CustomSessionInterface()

    return app
