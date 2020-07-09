package wafec.testing.execution.openstack.monitors.cinder;

import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.Volume;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class VolumeCreateMonitor extends AbstractVolumeMonitor {
    public VolumeCreateMonitor() {
        super();
        timeout = TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
    }

    @Override
    protected boolean assertTrue(Volume volume) {
        return Optional.ofNullable(volume)
                .map(Volume::getStatus)
                .map(String::toLowerCase)
                .equals(Optional.of("available"));
    }
}
