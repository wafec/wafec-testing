from flask import Flask
from flavors import flavors_api

app = Flask(__name__)

app.register_blueprint(flavors_api, url_prefix='/api/nova')


@app.route('/')
def hello():
    return 'Hello World!'


if __name__ == '__main__':
    app.run()
