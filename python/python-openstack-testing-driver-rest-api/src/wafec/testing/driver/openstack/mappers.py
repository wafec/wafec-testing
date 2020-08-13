
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


def network_to_view(network):
    return {
        'id': network['id'],
        'name': network['name'],
        'project': network['project_id']
    }


def server_to_view(server):
    image_id = None
    if server.image and 'id' in server.image:
        image_id = server.image['id']
    return {"name": server.name,
            "image": image_id,
            "flavor": server.flavor['id'],
            "id": server.id,
            "status": server.status,
            "power_state": str(getattr(server, 'OS-EXT-STS:power_state')),
            "network": "",
            "task_state": str(getattr(server, 'OS-EXT-STS:task_state')),
            "vm_state": str(getattr(server, 'OS-EXT-STS:vm_state')),
            "host_id": server.hostId,
            "host": str(getattr(server, 'OS-EXT-SRV-ATTR:host')),
            "created": str(server.created)
    }


def attachment_to_view(attachment):
    return {
        'attachment_id': attachment['attachment_id'],
        'server_id': attachment['server_id'],
        'host_name': attachment['host_name'],
        'device': attachment['device']
    }


def volume_to_view(volume):
    return {
        "id": volume.id,
        "name": volume.name,
        "size": str(volume.size),
        "availability_zone": volume.availability_zone,
        "status": volume.status,
        "attachments": list(map(lambda a: attachment_to_view(a), volume.attachments))
    }
