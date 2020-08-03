package wafec.testing.execution.openstack.monitors.nova;

import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.OpenStackClientBadRequestException;
import wafec.testing.driver.openstack.client.Server;
import wafec.testing.execution.ConditionWaitOnErrorResult;
import wafec.testing.execution.openstack.ResourceNotFoundException;

import java.util.concurrent.TimeUnit;

@Component
public class ServerDeleteMonitor extends AbstractServerMonitor {
    private boolean isValid = false;

    public ServerDeleteMonitor() {
        super();
        timeout = TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
    }

    @Override
    protected boolean assertTrue(Server server) {
        return isValid;
    }

    @Override
    protected ConditionWaitOnErrorResult onError(Exception exc) {
        if (exc instanceof OpenStackClientBadRequestException) {
            var badExc = (OpenStackClientBadRequestException) exc;
            if (badExc.getStatusCode() == 404) {
                isValid = true;
                return ConditionWaitOnErrorResult.propagatePass(true);
            }
        }
        return super.onError(exc);
    }
}
