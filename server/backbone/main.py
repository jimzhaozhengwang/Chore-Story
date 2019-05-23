from flask import Blueprint, render_template
from flask_login import login_required, current_user

from .decorators import admin_login_required
from . import create_app

main = Blueprint('main', __name__)


@main.route('/')
def index():
    return render_template('index.html')


@main.route('/profile')
@login_required
def profile():
    return render_template('profile.html', name=current_user.name)


@main.route('/admin')
@admin_login_required
def admin_example():
    return render_template('profile.html', name='admin ' + current_user.name)


if __name__ == '__main__':
    create_app().run(host='0.0.0.0', port=4000)
