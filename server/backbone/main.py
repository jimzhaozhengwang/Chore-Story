from flask import render_template
from flask_login import login_required, current_user

from . import create_app
from .decorators import admin_login_required
from .views import main


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

