from . import *


@api_bp.route('/until_next_level')
@child_login_required
@json_content_only
def until_next_level(current_level):
    # For now this is a constant 12 xp / level, but this
    #  function makes us able to change this on the fly
    return {'exp': 12}


