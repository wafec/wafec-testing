package wafec.testing.execution.openstack.monitors.nova;

import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.Server;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ServerCreateMonitor extends AbstractServerMonitor {

    public ServerCreateMonitor() {
        super();
        timeout = TimeUnit.MILLISECONDS.convert(3, TimeUnit.MINUTES);
    }

    @Override
    protected boolean assertTrue(Server server) {
        return Optional.ofNullable(server.getPowerState()).equals(Optional.of("1")) &&
                Optional.ofNullable(server.getStatus()).map(String::toLowerCase)
                .equals(Optional.of("active"));
    }
}
