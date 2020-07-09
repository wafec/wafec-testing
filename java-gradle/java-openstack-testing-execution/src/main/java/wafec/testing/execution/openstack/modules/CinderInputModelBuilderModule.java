package wafec.testing.execution.openstack.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.core.MapGet;
import wafec.testing.driver.openstack.client.ServerClient;
import wafec.testing.driver.openstack.client.Volume;
import wafec.testing.driver.openstack.client.VolumeClient;
import wafec.testing.execution.*;
import wafec.testing.execution.openstack.IllegalResourceStateException;
import wafec.testing.execution.openstack.ResourceNotCreatedException;
import wafec.testing.execution.openstack.ResourceNotFoundException;
import wafec.testing.execution.openstack.TestDriverContextUtils;
import wafec.testing.execution.openstack.monitors.cinder.AbstractVolumeMonitor;
import wafec.testing.execution.openstack.monitors.cinder.VolumeCreateMonitor;
import wafec.testing.execution.openstack.monitors.cinder.VolumeDeleteMonitor;

import java.util.List;
import java.util.Map;

@Component
public class CinderInputModelBuilderModule extends TestDriverInputModelBuilderModule {
    public static final String SOURCE = CinderInputModelBuilderModule.class.getName();

    @Autowired
    private VolumeClient volumeClient;
    @Autowired
    private ServerClient serverClient;

    @Autowired
    private VolumeCreateMonitor volumeCreateMonitor;
    @Autowired
    private VolumeDeleteMonitor volumeDeleteMonitor;

    @Override
    protected void configure(TestDriverInputModelBuilder builder) {
        builder
                .map("volumeCreate", this::volumeCreate)
                .map("volumeDelete", this::volumeDelete);
    }

    @PreCondition(target = PreCondition.SECURED)
    private List<TestDriverObservedOutput> volumeCreate(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException, TestDriverException {
        var testData = handler.getTestData();
        var key = TestDriverContextUtils.getKey(handler);
        var builder = new TestDriverObservedOutputBuilder();
        builder.and().log(SOURCE, "start volumeCreate");
        Volume volume = new Volume();
        volume.setName(testData.getValue("name"));
        volume.setAvailabilityZone(testData.getValue("availability_zone"));
        volume.setSize(testData.getValue("size"));
        var volumeResult = volumeClient.create(key, volume);
        if (volumeResult == null) {
            throw new ResourceNotCreatedException(String.format("volume %s", testData.getValue("name")))
                    .addAll(builder);
        }
        var monitorResult = volumeCreateMonitor.monitor(MapGet.of(Map.of("key", key, "volumeId", volume.getId())));
        builder.join(monitorResult.getObservedOutputs());
        if (!monitorResult.isExitSuccess())
            throw new IllegalResourceStateException(String.format("volume %s", testData.getValue("name")))
            .addAll(builder);
        return builder.and().success(SOURCE, "volumeCreate").buildList();
    }

    @PreCondition(target = PreCondition.SECURED)
    private List<TestDriverObservedOutput> volumeAction(TestDriverInputFunctionHandler handler,
                                                        AbstractVolumeMonitor volumeMonitor,
                                                        String actionName,
                                                        OpenStackModuleActionConsumer<Volume> volumeConsumer)
        throws TestDataValueNotFoundException, TestDriverException {
        var builder = new TestDriverObservedOutputBuilder();
        var testData = handler.getTestData();
        var key = TestDriverContextUtils.getKey(handler);
        var volumeResult = volumeClient.findByName(key, testData.getValue("name"));
        if (volumeResult == null || volumeResult.size() == 0)
            throw new ResourceNotFoundException(String.format("volume %s", testData.getValue("name")))
            .addAll(builder);
        var volume = volumeResult.get(0);
        volumeConsumer.accept(volume);
        var monitorResult = volumeMonitor.monitor(MapGet.of(Map.of("key", key, "volumeId", volume.getId())));
        builder.join(monitorResult.getObservedOutputs());
        if (!monitorResult.isExitSuccess())
            throw new IllegalResourceStateException(String.format("volume %s", testData.getValue("name")))
            .addAll(builder);
        return builder.and().success(SOURCE, actionName).buildList();
    }

    @PreCondition(target = PreCondition.SECURED)
    private List<TestDriverObservedOutput> volumeDelete(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException, TestDriverException {
        return volumeAction(handler, volumeDeleteMonitor, "volumeDelete", (volume) -> {
            var key = TestDriverContextUtils.getKey(handler);
            volumeClient.delete(volume.getId(), key);
        });
    }
}
