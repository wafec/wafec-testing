package wafec.testing.execution.openstack.monitors.nova;

import org.springframework.beans.factory.annotation.Autowired;
import wafec.testing.driver.openstack.client.FlavorClient;
import wafec.testing.driver.openstack.client.ImageClient;
import wafec.testing.driver.openstack.client.ServerClient;
import wafec.testing.execution.AbstractTestDriverMonitor;

public abstract class AbstractNovaMonitor extends AbstractTestDriverMonitor {
    @Autowired
    protected FlavorClient flavorClient;
    @Autowired
    protected ImageClient imageClient;
    @Autowired
    protected ServerClient serverClient;
}
