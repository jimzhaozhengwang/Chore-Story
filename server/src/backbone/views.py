from flask import Blueprint

# Blueprints for our flask app

api_bp = Blueprint('api', __name__)
auth = Blueprint('auth', __name__)
main = Blueprint('main', __name__)
