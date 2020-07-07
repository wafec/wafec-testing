from flask import Blueprint, jsonify, request, abort
import commons
import mappers
import errors
from glanceclient.exc import Conflict, BadRequest
import base64
import tempfile

images_api = Blueprint('images', __name__)


@images_api.route('/', methods=['GET'])
def get():
    kwargs = {}
    name = request.args.get('name')
    if name:
        kwargs['filters'] = {'name': name}
    glance = commons.glance_client(request.args.get('key'))
    images = glance.images.list(**kwargs)
    return jsonify(list(map(lambda i: mappers.image_to_view(i), images))), 201


@images_api.route('/<id>/', methods=['GET'])
def get_by_id(id):
    glance = commons.glance_client(request.args.get('key'))
    images = glance.images.list(filters={'id': id})
    if not images or not len(images):
        return errors.http_notfound()
    image = images[0]
    return jsonify(mappers.image_to_view(image)), 201


@images_api.route('/', methods=['POST'])
def post():
    glance = commons.glance_client(request.args.get('key'))
    image = glance.images.create(name=request.json['name'],
                                 disk_format=request.json['disk_format'],
                                 container_format=request.json['container_format'])
    if image:
        data = request.json["data"]
        decoded_data = base64.b64decode(data)
        with tempfile.NamedTemporaryFile() as temp:
            temp.write(decoded_data)
            glance.images.upload(image.id, temp)
    return jsonify({'id': image.id}), 201


@images_api.route('/<id>/', methods=['DELETE'])
def delete(id):
    glance = commons.glance_client(request.args.get('key'))
    glance.images.delete(id)
    return jsonify(success=True)


@images_api.errorhandler(Conflict)
@images_api.errorhandler(BadRequest)
def client_error(e):
    response = jsonify({
        'message': str(e)
    })
    response.status_code = 400
    return response
