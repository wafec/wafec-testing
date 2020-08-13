from flask import jsonify


def http_notfound(message=None):
    response = jsonify({
        'message': message if message else 'Resource not found'
    })
    response.status_code = 404
    return response