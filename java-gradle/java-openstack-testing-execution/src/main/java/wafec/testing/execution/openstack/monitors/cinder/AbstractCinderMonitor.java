package wafec.testing.execution.openstack.monitors.cinder;

import org.springframework.beans.factory.annotation.Autowired;
import wafec.testing.driver.openstack.client.ServerClient;
import wafec.testing.driver.openstack.client.VolumeClient;
import wafec.testing.execution.AbstractTestDriverMonitor;

public abstract class AbstractCinderMonitor extends AbstractTestDriverMonitor {
    @Autowired
    protected VolumeClient volumeClient;
    @Autowired
    protected ServerClient serverClient;
}
