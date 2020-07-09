package wafec.testing.execution.openstack.monitors.nova;

import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.Server;

import java.util.Optional;

@Component
public class ServerResumeMonitor extends AbstractServerMonitor {
    @Override
    protected boolean assertTrue(Server server) {
        return Optional.ofNullable(server)
                .map(Server::getStatus)
                .map(String::toLowerCase)
                .equals(Optional.of("active"));
    }
}
