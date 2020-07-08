from flask import Blueprint, jsonify, request
import commons
import mappers
import errors
from novaclient.exceptions import Conflict, BadRequest, NotFound

servers_api = Blueprint('servers', __name__)


@servers_api.route("/", methods=['GET'])
def get():
    search_opts = {}
    name = request.args.get('name')
    if name:
        search_opts['name'] = name
    nova = commons.nova_client(request.args.get('key'))
    servers = nova.servers.list(search_opts=search_opts)
    return jsonify(list(map(lambda server: mappers.server_to_view(server), servers))), 201


@servers_api.route('/<id>', methods=['GET'])
def get_by_id(id):
    nova = commons.nova_client(request.args.get('key'))
    servers = nova.servers.list(search_opts={'id': id})
    if servers and len(servers):
        return mappers.server_to_view(servers[0]), 201
    else:
        return errors.http_notfound()


@servers_api.route('/', methods=['POST'])
def post():
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.create(name=request.json['name'],
                                 flavor=request.json['flavor'],
                                 image=request.json['image'],
                                 network=request.json['network'])
    return jsonify({'id': server.id})


@servers_api.route('/<id>/', methods=['DELETE'])
def delete(id):
    nova = commons.nova_client(request.args.get('key'))
    nova.servers.delete(id)
    return jsonify(success=True)


@servers_api.route('/<id>/actions/pause', methods=['POST'])
def pause(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    if server:
        server.pause()
        return jsonify(success=True)
    else:
        return errors.http_notfound()


@servers_api.route('/<id>/actions/unpause', methods=['POST'])
def unpause(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    if server:
        server.unpause()
        return jsonify(success=True)
    else:
        return errors.http_notfound()


@servers_api.route('/<id>/actions/shelve', methods=['POST'])
def shelve(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    if server:
        server.shelve()
        return jsonify(success=True)
    else:
        return errors.http_notfound()


@servers_api.route('/<id>/actions/unshelve', methods=['POST'])
def unshelve(id):
    nova = commons.nova_client(request.args.get('key'))
    nova.servers.unshelve(id)
    return jsonify(success=True)


@servers_api.route('/<id>/actions/stop', methods=['POST'])
def stop(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    server.stop()
    return jsonify(success=True)


@servers_api.route('/<id>/actions/start', methods=['POST'])
def start(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    server.start()
    return jsonify(success=True)


@servers_api.route('/<id>/actions/suspend', methods=['POST'])
def suspend(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    server.suspend()
    return jsonify(success=True)


@servers_api.route('/<id>/actions/resume', methods=['POST'])
def resume(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    server.resume()
    return jsonify(success=True)


@servers_api.route('/<id>/actions/resize', methods=['POST'])
def resize(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    server.resize(flavor=request.json['id'])
    return jsonify(success=True)


@servers_api.route('/<id>/actions/resize/confirm', methods=['POST'])
def resize_confirm(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    action = request.args.get('action')
    if action == 'revert':
        server.revert_resize()
    else:
        server.confirm_resize()
    return jsonify(success=True)


@servers_api.route('/<id>/actions/migrate', methods=['POST'])
def migrate(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    nova.servers.migrate(server)
    return jsonify(success=True)


@servers_api.route('/<id>/actions/live-migrate', methods=['POST'])
def live_migrate(id):
    nova = commons.nova_client(request.args.get('key'))
    server = nova.servers.find(id=id)
    nova.servers.live_migrate(server=server, host=None, block_migration=False, disk_over_commit=False)
    return jsonify(success=True)


@servers_api.errorhandler(Conflict)
@servers_api.errorhandler(BadRequest)
@servers_api.errorhandler(NotFound)
def client_error(e):
    response = jsonify({
        'message': str(e)
    })
    if isinstance(e, NotFound):
        response.status_code = 404
    elif isinstance(e, BadRequest):
        response.status_code = 400
    elif isinstance(e, Conflict):
        response.status_code = 409
    else:
        response.status_code = 400
    return response
