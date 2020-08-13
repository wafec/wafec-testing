from flask import request, Blueprint, jsonify
from wafec.testing.driver.openstack import commons
from wafec.testing.driver.openstack import mappers
from wafec.testing.driver.openstack import errors
from cinderclient.exceptions import BadRequest, NotFound

volumes_api = Blueprint('volumes', __name__)


@volumes_api.route('/', methods=['GET'])
def get():
    cinder = commons.cinder_client(request.args.get('key'))
    name = request.args.get('name')
    search_opts = {}
    if name:
        search_opts['name'] = name
    volumes = cinder.volumes.list(search_opts=search_opts)
    return jsonify(list(map(lambda volume: mappers.volume_to_view(volume), volumes))), 201


@volumes_api.route('/<id>', methods=['GET'])
def get_by_id(id):
    cinder = commons.cinder_client(request.args.get('key'))
    volumes = cinder.volumes.list(search_opts={'id': id})
    if volumes and len(volumes):
        print(dir(volumes[0]))
        print(volumes[0].attachments)
        return jsonify(mappers.volume_to_view(volumes[0])), 201
    else:
        return errors.http_notfound()


@volumes_api.route('/', methods=['POST'])
def post():
    cinder = commons.cinder_client(request.args.get('key'))
    volume = cinder.volumes.create(name=request.json['name'],
                                   size=int(request.json['size']),
                                   availability_zone=request.json['availability_zone'])
    return jsonify({'id': volume.id})


@volumes_api.route('/<id>', methods=['DELETE'])
def delete(id):
    cinder = commons.cinder_client(request.args.get('key'))
    volumes = cinder.volumes.list(search_opts={'id': id})
    cinder.volumes.delete(volumes[0])
    return jsonify(success=True)


@volumes_api.route('/<id>/actions/extend', methods=['POST'])
def extend(id):
    cinder = commons.cinder_client(request.args.get('key'))
    volumes = cinder.volumes.list(search_opts={'id': id})
    if not volumes or not len(volumes):
        return errors.http_notfound()
    volume = volumes[0]
    cinder.volumes.extend(volume, int(request.args.json['size']))
    return jsonify(success=True)


@volumes_api.route('/<id>/actions/attach', methods=['POST'])
def attach(id):
    cinder = commons.cinder_client(request.args.get('key'))
    volumes = cinder.volumes.list(search_opts={'id': id})
    if not volumes or not len(volumes):
        return errors.http_notfound()
    volume = volumes[0]
    cinder.volumes.attach(volume, request.json['server'], mountpoint=request.json['mountpoint'])
    return jsonify(success=True)


@volumes_api.route('/<id>/actions/detach', methods=['POST'])
def detach(id):
    cinder = commons.cinder_client(request.args.get('key'))
    volumes = cinder.volumes.list(search_opts={'id': id})
    if not volumes or not len(volumes):
        return errors.http_notfound()
    volume = volumes[0]
    cinder.volumes.detach(volume)
    return jsonify(success=True)


@volumes_api.errorhandler(NotFound)
@volumes_api.errorhandler(BadRequest)
def client_error(e):
    response = jsonify({
        'message': str(e)
    })
    if isinstance(e, NotFound):
        response.status_code = 404
    else:
        response.status_code = 400
    return response

