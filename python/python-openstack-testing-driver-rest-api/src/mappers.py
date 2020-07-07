"""Mappers"""


def flavor_to_view(flavor):
    return {
        'id': flavor.id,
        'name': flavor.name,
        'ram': flavor.ram,
        'vcpus': flavor.vcpus,
        'disk': flavor.disk
    }