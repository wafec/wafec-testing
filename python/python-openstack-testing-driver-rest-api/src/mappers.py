

def flavor_to_view(flavor):
    return {
        'id': flavor.id,
        'name': flavor.name,
        'ram': flavor.ram,
        'vcpus': flavor.vcpus,
        'disk': flavor.disk
    }


def image_to_view(image):
    return {
        'id': image.id,
        'name': image.name,
        'disk_format': image.disk_format,
        'container_format': image.container_format
    }