package wafec.testing.execution.openstack.monitors.nova;

import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.Server;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ServerPauseMonitor extends AbstractServerMonitor {
    public ServerPauseMonitor() {
        super();
        timeout = TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS);
    }

    @Override
    protected boolean assertTrue(Server server) {
        return
                Optional.ofNullable(server)
                .map(Server::getStatus)
                .map(String::toLowerCase)
                .equals(Optional.of("paused"));
    }
}