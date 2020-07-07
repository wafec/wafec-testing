from flask import Blueprint, jsonify, request, abort
import commons
import mappers
import errors
from novaclient.exceptions import BadRequest, Conflict

flavors_api = Blueprint('flavors', __name__)


@flavors_api.route('/', methods=['GET'])
def get():
    nova = commons.nova_client(request.args.get('key'))
    name = request.args.get('name')
    if name:
        flavors = nova.flavors.findall(name=name)
    else:
        flavors = nova.flavors.list()
    return jsonify(list(map(lambda flavor: mappers.flavor_to_view(flavor), flavors))), 201


@flavors_api.route('/<id>/', methods=['GET'])
def get_by_id(id):
    nova = commons.nova_client(request.args.get('key'))
    flavor = nova.flavors.find(id=id)
    if not flavor:
        return errors.http_notfound()
    return jsonify(mappers.flavor_to_view(flavor)), 201


@flavors_api.route('/', methods=['POST'])
def post():
    nova = commons.nova_client(request.args.get('key'))
    flavor = nova.flavors.create(name=request.json['name'],
                                 ram=request.json['ram'],
                                 vcpus=request.json['vcpus'],
                                 disk=request.json['disk'])
    return jsonify({'id': flavor.id}), 201


@flavors_api.route('/<id>/', methods=['DELETE'])
def delete(id):
    nova = commons.nova_client(request.args.get('key'))
    flavor = nova.flavors.find(id=id)
    nova.flavors.delete(flavor)
    return jsonify(success=True)


@flavors_api.errorhandler(Conflict)
@flavors_api.errorhandler(BadRequest)
def client_error(e):
    response = jsonify({
        'message': str(e)
    })
    response.status_code = 400
    return response
