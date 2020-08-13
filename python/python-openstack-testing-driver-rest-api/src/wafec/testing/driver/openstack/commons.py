from wafec.testing.driver.openstack.models import Key, Session
from keystoneauth1.identity import v3
from keystoneauth1 import session
from novaclient.client import Client as novaclient
from glanceclient.client import Client as glanceclient
from neutronclient.v2_0.client import Client as neutronclient
from cinderclient.client import Client as cinderclient
import base64
import datetime

NOVA_VERSION = '2.1'
GLANCE_VERSION = '2'
NEUTRON_VERSION = '2'
CINDER_VERSION = '3'


def decode_key(key):
    decoded_string = base64.b64decode(key)
    return decoded_string.decode('utf-8')


def keystone_session(key):
    alchemy_session = Session()
    key_data = alchemy_session.query(Key).filter(Key.pass_value == decode_key(key)).first()
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


def neutron_client(key):
    key_session = keystone_session(key)
    client = neutronclient(session=key_session)
    return client


def cinder_client(key):
    key_session = keystone_session(key)
    client = cinderclient(CINDER_VERSION, session=key_session)
    return client


def str_to_date(value):
    # e.g.: 2020-07-16T15:51:50Z
    return datetime.datetime.strptime(value, '%Y-%m-%dT%H:%M:%SZ')