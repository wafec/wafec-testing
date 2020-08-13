from flask import Flask, jsonify
from wafec.testing.driver.openstack.flavors import flavors_api
from wafec.testing.driver.openstack.images import images_api
from wafec.testing.driver.openstack.networks import networks_api
from wafec.testing.driver.openstack.servers import servers_api
from wafec.testing.driver.openstack.volumes import volumes_api
import datetime

app = Flask(__name__)

app.url_map.strict_slashes = False

app.register_blueprint(flavors_api, url_prefix='/api/nova/flavors')
app.register_blueprint(images_api, url_prefix='/api/glance/images')
app.register_blueprint(networks_api, url_prefix='/api/neutron/networks')
app.register_blueprint(servers_api, url_prefix='/api/nova/servers')
app.register_blueprint(volumes_api, url_prefix='/api/cinder/volumes')


@app.route('/')
def hello():
    return 'Hello World!'


@app.route('/health-check')
def health_check():
    return jsonify({
        "status": "up",
        "date": datetime.datetime.utcnow().strftime('%Y-%m-%dT%H:%M:%SZ')
    }), 201


if __name__ == '__main__':
    app.run(debug=False)
