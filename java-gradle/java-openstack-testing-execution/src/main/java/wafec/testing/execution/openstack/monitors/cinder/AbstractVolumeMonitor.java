package wafec.testing.execution.openstack.monitors.cinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wafec.testing.core.MapGet;
import wafec.testing.driver.openstack.client.OpenStackClientException;
import wafec.testing.driver.openstack.client.Volume;
import wafec.testing.driver.openstack.client.VolumeUtils;
import wafec.testing.execution.*;
import wafec.testing.execution.openstack.OpenStackTestDriverException;
import wafec.testing.execution.openstack.ResourceNotFoundException;

public abstract class AbstractVolumeMonitor extends AbstractCinderMonitor {
    public static final String SOURCE = AbstractVolumeMonitor.class.getName();

    protected abstract boolean assertTrue(Volume volume);

    Volume last;

    protected long timeout = ConditionWaiter.DEFAULT_TIMEOUT;
    protected long interval = ConditionWaiter.DEFAULT_INTERVAL;

    private Logger logger = LoggerFactory.getLogger(AbstractVolumeMonitor.class);

    @Override
    protected TestDriverMonitorResult monitorInternal(MapGet mapGet, TestDriverObservedOutputBuilder builder) throws Exception {
        last = null;
        try {
            var waitResult = new ConditionWaiter(timeout, interval).onError(this::onError).waitForConditionBeTrue(() -> {
                var volume = volumeClient.findById(mapGet.<String>get("volumeId"), mapGet.<String>get("key"));
                if (volume == null)
                    throw new ResourceNotFoundException(String.format("volume %s", mapGet.<String>get("volumeId")));
                if (last == null || VolumeUtils.hasChangedStatus(last, volume)) {
                    builder.and().change(SOURCE, volume.toString());
                }
                last = volume;
                logger.debug(last.toString());
                return assertTrue(volume);
            });
            waitResult.getErrors().forEach(e -> builder.and().error(SOURCE, e));
            return new TestDriverMonitorResult(waitResult.isExitSuccess(), builder.buildList());
        } catch (ConditionWaitStopException exc) {
            return new TestDriverMonitorResult(exc.exitSuccess(), builder.buildList());
        }
    }

    protected ConditionWaitOnErrorResult onError(Exception exc) {
        if (exc instanceof OpenStackClientException || exc instanceof OpenStackTestDriverException)
            return ConditionWaitOnErrorResult.propagateFail(true);
        return ConditionWaitOnErrorResult.propagatePass(false);
    }
}
