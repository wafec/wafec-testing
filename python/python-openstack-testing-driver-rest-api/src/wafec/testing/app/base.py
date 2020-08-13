from argparse import ArgumentParser


class BaseCmd():
    def __init__(self, parser, is_callable=False):
        self._parser = parser if parser is not None else ArgumentParser()
        self._subparsers = self._parser.add_subparsers()
        if is_callable:
            self._parser.set_defaults(func=self._call)

    def add_subcommand(self, subcommand_class):
        subparser = self._subparsers.add_parser(subcommand_class.name)
        subcommand = subcommand_class(subparser)
        return subcommand

    @property
    def parser(self):
        return self._parser

    def _call(self, args):
        if 'call' in dir(self) and callable(getattr(self, 'call')):
            getattr(self, 'call')(args)

    def parse_args(self):
        args = self._parser.parse_args()
        if 'func' in args:
            args.func(args)
