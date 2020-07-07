from flask import Flask
from flavors import flavors_api
from images import images_api

app = Flask(__name__)

app.register_blueprint(flavors_api, url_prefix='/api/nova/flavors')
app.register_blueprint(images_api, url_prefix='/api/glance/images')


@app.route('/')
def hello():
    return 'Hello World!'


if __name__ == '__main__':
    app.run(debug=True)
