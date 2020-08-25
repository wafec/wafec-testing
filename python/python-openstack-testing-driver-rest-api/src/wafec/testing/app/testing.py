from wafec.testing.app.base import BaseCmd
from wafec.testing.driver.openstack.main import app


class Testing(BaseCmd):
    name = 'testing'

    def __init__(self):
        BaseCmd.__init__(self, None)
        self.add_subcommand(TestingDriver)


class TestingDriver(BaseCmd):
    name = 'driver'

    def __init__(self, handler):
        BaseCmd.__init__(self, handler)
        self.add_subcommand(TestingDriverOpenStack)


class TestingDriverOpenStack(BaseCmd):
    name = 'openstack'

    def __init__(self, handler):
        BaseCmd.__init__(self, handler)
        self.add_subcommand(TestingDriverOpenStackStart)


class TestingDriverOpenStackStart(BaseCmd):
    name = 'start'

    def __init__(self, handler):
        BaseCmd.__init__(self, handler, is_callable=True)

    def call(self, args):
        app.run()


if __name__ == '__main__':
    parser = Testing()
    parser.parse_args()
