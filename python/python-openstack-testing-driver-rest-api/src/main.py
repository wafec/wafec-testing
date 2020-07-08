from flask import Flask
from flavors import flavors_api
from images import images_api
from networks import networks_api
from servers import servers_api

app = Flask(__name__)

app.url_map.strict_slashes = False

app.register_blueprint(flavors_api, url_prefix='/api/nova/flavors')
app.register_blueprint(images_api, url_prefix='/api/glance/images')
app.register_blueprint(networks_api, url_prefix='/api/neutron/networks')
app.register_blueprint(servers_api, url_prefix='/api/nova/servers')


@app.route('/')
def hello():
    return 'Hello World!'


if __name__ == '__main__':
    app.run(debug=True)
