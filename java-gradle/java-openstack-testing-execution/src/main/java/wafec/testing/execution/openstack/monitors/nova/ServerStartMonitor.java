package wafec.testing.execution.openstack.monitors.nova;

import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.Server;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ServerStartMonitor extends AbstractServerMonitor {
    public ServerStartMonitor() {
        super();
        timeout = TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES);
    }

    @Override
    protected boolean assertTrue(Server server) {
        return Optional.ofNullable(server)
                .map(Server::getStatus)
                .map(String::toLowerCase)
                .equals(Optional.of("active"));
    }
}
