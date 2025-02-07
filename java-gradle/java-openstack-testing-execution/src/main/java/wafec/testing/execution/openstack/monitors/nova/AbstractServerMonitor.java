package wafec.testing.execution.openstack.monitors.nova;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import wafec.testing.core.MapGet;
import wafec.testing.driver.openstack.client.OpenStackClientException;
import wafec.testing.driver.openstack.client.Server;
import wafec.testing.driver.openstack.client.ServerUtils;
import wafec.testing.execution.*;
import wafec.testing.execution.openstack.OpenStackTestDriverException;
import wafec.testing.execution.openstack.ResourceNotFoundException;
import wafec.testing.execution.openstack.ResourceOnErrorStateException;

import java.util.Optional;


public abstract class AbstractServerMonitor extends AbstractNovaMonitor {
    public static final String SOURCE = AbstractServerMonitor.class.getName();

    protected long timeout = ConditionWaiter.DEFAULT_TIMEOUT;
    protected long interval = ConditionWaiter.DEFAULT_INTERVAL;

    protected abstract boolean assertTrue(Server server);

    private Server last;

    Logger logger = LoggerFactory.getLogger(AbstractServerMonitor.class);

    public AbstractServerMonitor() {
        super();
    }

    @Override
    protected TestDriverMonitorResult monitorInternal(final MapGet mapGet, final TestDriverObservedOutputBuilder builder) throws
            Exception {
        logger.debug("start monitorInternal");
        last = null;
        try {
            var waitResult = new ConditionWaiter(timeout, interval).onError(this::onError).waitForConditionBeTrue(() -> {
                var server = serverClient.findById(mapGet.<String>get("serverId"), mapGet.<String>get("key"));
                if (server == null)
                    throw new ResourceNotFoundException(String.format("server %s", mapGet.<String>get("serverId")));
                if (last == null || ServerUtils.hasChangedState(last, server)) {
                    builder.and().change(SOURCE, ServerUtils.dumpState(server));
                }
                last = server;
                logger.debug(server.toString());
                if (stopOnErrorState(server))
                    throw new ResourceOnErrorStateException(server.toString());
                return assertTrue(server);
            });
            waitResult.getErrors().forEach(e -> builder.and().error(SOURCE, e));
            return new TestDriverMonitorResult(waitResult.isExitSuccess(), builder.buildList());
        } catch (ConditionWaitStopException exc) {
            return new TestDriverMonitorResult(exc.exitSuccess(), builder.buildList());
        }
    }

    protected ConditionWaitOnErrorResult onError(Exception exc) {
        if (exc instanceof OpenStackClientException || exc instanceof OpenStackTestDriverException) {
            return ConditionWaitOnErrorResult.propagateFail(true);
        }
        return ConditionWaitOnErrorResult.propagatePass(false);
    }

    protected boolean stopOnErrorState(Server server) {
        return Optional.ofNullable(server)
                .map(Server::getStatus)
                .map(String::toLowerCase)
                .equals(Optional.of("error"));
    }
}
