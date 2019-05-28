from flask import Flask, g
from flask.sessions import SecureCookieSessionInterface
from flask_login import LoginManager, user_loaded_from_request
from flask_sqlalchemy import SQLAlchemy
import click

# init SQLAlchemy so we can use it later in our models
db = SQLAlchemy()


def create_app():
    app = Flask(__name__)

    app.config['SECRET_KEY'] = 'CmxIn2GpX8CsD9KSVY7voBdFvlQV7MXaUW34jHXTyYDX7E'
    app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///db.sqlite'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

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
            api_key = api_key.replace('Basic ', '', 1)
            # TODO turn this back on
            # try:
            #     api_key = base64.b64decode(api_key)
            # except TypeError:
            #     pass
            user = (Parent.query.filter_by(api_key=api_key).first() or
                    Child.query.filter_by(api_key=api_key).first())
            if user:
                return user
        return None

    # blueprint for auth routes in our app
    from .auth import auth as auth_blueprint
    app.register_blueprint(auth_blueprint)

    # blueprint for api calls
    from .api import api_bp
    app.register_blueprint(api_bp, url_prefix='/api')

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
