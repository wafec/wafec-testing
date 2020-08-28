package wafec.testing.execution.openstack.monitors.nova;

import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.Server;

@Component
public class ServerRebuildMonitor extends AbstractServerMonitor {
    @Override
    protected boolean assertTrue(Server server) {
        return false;
    }
}
