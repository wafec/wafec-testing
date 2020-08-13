from flask import Blueprint, jsonify, request
from wafec.testing.driver.openstack import commons
from wafec.testing.driver.openstack import mappers
from wafec.testing.driver.openstack import errors

networks_api = Blueprint('networks', __name__)


@networks_api.route("/", methods=['GET'])
def get():
    neutron = commons.neutron_client(request.args.get('key'))
    kwargs = {}
    name = request.args.get('name')
    if name:
        kwargs['name'] = name
    networks = neutron.list_networks(**kwargs)
    if networks and networks['networks']:
        return jsonify(list(map(lambda network: mappers.network_to_view(network), networks['networks']))), 201
    else:
        return errors.http_notfound()


@networks_api.route("/<id>/", methods=['GET'])
def get_by_id(id):
    neutron = commons.neutron_client(request.args.get('key'))
    networks = neutron.list_networks(id=id)
    if networks and len(networks):
        return jsonify(mappers.network_to_view(networks[0]))
    else:
        return errors.http_notfound()


@networks_api.route("/", methods=['POST'])
def post():
    return jsonify(success=True)


@networks_api.route("/<id>/", methods=['DELETE'])
def delete(id):
    return jsonify(success=True)