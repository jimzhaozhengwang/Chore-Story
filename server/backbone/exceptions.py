class BackboneException(Exception):
    """This is an exception for when you want to simply return an error code and a message"""
    def __init__(self, error_code, message):
        self.error_code = error_code
        self.message = message
