package wafec.testing.execution.openstack.monitors.cinder;

import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.OpenStackClientBadRequestException;
import wafec.testing.driver.openstack.client.Volume;
import wafec.testing.execution.ConditionWaitOnErrorResult;

@Component
public class VolumeDeleteMonitor extends AbstractVolumeMonitor {
    private boolean isValid = false;

    public VolumeDeleteMonitor() {
        super();
    }

    @Override
    protected boolean assertTrue(Volume volume) {
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
