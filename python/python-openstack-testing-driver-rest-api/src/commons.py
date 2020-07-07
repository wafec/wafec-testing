from models import Key, Session
from keystoneauth1.identity import v3
from keystoneauth1 import session
from novaclient.client import Client as novaclient
from glanceclient.client import Client as glanceclient
import base64

NOVA_VERSION = '2.1'
GLANCE_VERSION = '2'


def decode_key(key):
    decoded_string = base64.b64decode(key)
    return int(decoded_string)


def keystone_session(key):
    alchemy_session = Session()
    key_data = alchemy_session.query(Key).filter(Key.id == decode_key(key)).first()
    auth = v3.Password(auth_url=key_data.auth_url,
                       username=key_data.username,
                       password=key_data.password,
                       project_name=key_data.project_name,
                       project_domain_name=key_data.project_domain_name,
                       user_domain_name=key_data.user_domain_name)
    key_session = session.Session(auth=auth)
    return key_session


def nova_client(key):
    key_session = keystone_session(key)
    client = novaclient(NOVA_VERSION, session=key_session)
    return client


def glance_client(key):
    key_session = keystone_session(key)
    client = glanceclient(GLANCE_VERSION, session=key_session)
    return client