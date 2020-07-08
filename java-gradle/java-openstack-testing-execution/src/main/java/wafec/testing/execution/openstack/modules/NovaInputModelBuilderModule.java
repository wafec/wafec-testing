package wafec.testing.execution.openstack.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.core.MapGet;
import wafec.testing.driver.openstack.client.*;
import wafec.testing.execution.*;
import wafec.testing.execution.openstack.*;
import wafec.testing.execution.openstack.monitors.nova.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Component
public class NovaInputModelBuilderModule extends TestDriverInputModelBuilderModule {
    public static final String SOURCE = NovaInputModelBuilderModule.class.getName();

    @Autowired
    private FlavorClient flavorClient;
    @Autowired
    private ImageClient imageClient;
    @Autowired
    private NetworkClient networkClient;
    @Autowired
    private ServerClient serverClient;

    @Autowired
    private ServerCreateMonitor serverCreateMonitor;
    @Autowired
    private ServerPauseMonitor serverPauseMonitor;
    @Autowired
    private ServerUnpauseMonitor serverUnpauseMonitor;
    @Autowired
    private ServerShelveMonitor serverShelveMonitor;
    @Autowired
    private ServerUnshelveMonitor serverUnshelveMonitor;
    @Autowired
    private ServerDeleteMonitor serverDeleteMonitor;

    @Override
    protected void configure(TestDriverInputModelBuilder builder) {
        builder
                .map("flavorCreate", this::flavorCreate)
                .map("imageCreate", this::imageCreate)
                .map("serverCreate", this::serverCreate)
                .map("serverPause", this::serverPause)
                .map("serverUnpause", this::serverUnpause)
                .map("serverShelve", this::serverShelve)
                .map("serverUnshelve", this::serverUnshelve)
                .map("serverDelete", this::serverDelete);
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> flavorCreate(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException, TestDriverException {
        var testData = handler.getTestData();
        var key = TestDriverContextUtils.getKey(handler);
        var builder = new TestDriverObservedOutputBuilder();
        builder.and().log(SOURCE, "start flavorCreate");
        Flavor data = new Flavor();
        data.setName(testData.getValue("name"));
        data.setRam(testData.getValueOpt("ram").map(Integer::parseInt).get());
        data.setVcpus(testData.getValueOpt("vcpus").map(Integer::parseInt).get());
        data.setDisk(testData.getValueOpt("disk").map(Integer::parseInt).get());
        var result = flavorClient.create(key, data);
        if (result == null)
            throw new ResourceNotCreatedException(String.format("flavor %s", testData.getValue("name")))
                    .addAll(builder);
        return builder.and().success(SOURCE, "flavorCreate").buildList();
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> imageCreate(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException, TestDriverException {
        var testData = handler.getTestData();
        var key = TestDriverContextUtils.getKey(handler);
        var builder = new TestDriverObservedOutputBuilder();
        builder.and().log(SOURCE, "start imageCreate");
        Image data = new Image();
        data.setName(testData.getValue("name"));
        data.setDiskFormat(testData.getValue("disk_format"));
        data.setContainerFormat(testData.getValue("container_format"));
        try {
            var dataBytes = Files.readAllBytes(Paths.get(testData.getValue("data_file")));
            data.setData(Base64.getEncoder().encodeToString(dataBytes));
        } catch (IOException exc) {
            throw new IllegalArgumentException("Could not read data file", exc);
        }
        var result = imageClient.create(key, data);
        if (result == null)
            throw new ResourceNotCreatedException(String.format("image %s", testData.getValue("name")))
                    .addAll(builder);
        return builder.and().success(SOURCE, "imageCreate").buildList();
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> serverCreate(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException, TestDriverException {
        var testData = handler.getTestData();
        var key = TestDriverContextUtils.getKey(handler);
        var builder = new TestDriverObservedOutputBuilder();
        builder.and().log(SOURCE, "start serverCreate");
        var flavorResult = flavorClient.findByName(key, testData.getValue("flavor"));
        var imageResult = imageClient.findByName(key, testData.getValue("image"));
        var networkResult = networkClient.findByName(key, testData.getValue("network"));
        if (flavorResult == null || imageResult == null || networkResult == null ||
                Stream.of(flavorResult, imageResult, networkResult).anyMatch(list -> list.size() == 0)) {
            throw new ResourceNotFoundException(String.format("(flavor %s | image %s | network %s)",
                    testData.getValue("flavor"), testData.getValue("image"), testData.getValue("network")))
                    .addAll(builder);
        }
        Server data = new Server();
        data.setName(testData.getValue("name"));
        data.setFlavor(flavorResult.get(0).getId());
        data.setImage(imageResult.get(0).getId());
        data.setNetwork(networkResult.get(0).getId());
        var result = serverClient.create(key, data);
        if (result == null)
            throw new ResourceNotCreatedException(String.format("server %s", testData.getValue("name")))
                    .addAll(builder);
        var monitorResult = serverCreateMonitor.monitor(MapGet.of(Map.of("serverId", result.getId(),
                "key", key)));
        builder.join(monitorResult.getObservedOutputs());
        if (!monitorResult.isExitSuccess())
            throw new ResourceNotCreatedException(String.format("server %s", testData.getValue("name")))
                    .addAll(builder);
        return builder.and().success(SOURCE, "serverCreate").buildList();
    }

    private List<TestDriverObservedOutput> serverAction(TestDriverInputFunctionHandler handler,
                                                        AbstractServerMonitor serverMonitor,
                                                        String actionName,
                                                        Consumer<Server> serverConsumer) throws
            TestDataValueNotFoundException, TestDriverException {
        var testData = handler.getTestData();
        var key = TestDriverContextUtils.getKey(handler);
        var builder = new TestDriverObservedOutputBuilder();
        builder.and().log(SOURCE, actionName);
        var serverResult = serverClient.findByName(key, testData.getValue("name"));
        if (serverResult == null || serverResult.size() == 0)
            throw new ResourceNotFoundException(String.format("server %s", testData.getValue("name")))
            .addAll(builder);
        var server = serverResult.get(0);
        serverConsumer.accept(server);
        var monitorResult = serverMonitor.monitor(MapGet.of(Map.of("key", key, "serverId", server.getId())));
        builder.join(monitorResult.getObservedOutputs());
        if (!monitorResult.isExitSuccess())
            throw new IllegalResourceStateException(String.format("server %s", testData.getValue("name")))
            .addAll(builder);
        return builder.and().success(SOURCE, actionName).buildList();
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> serverPause(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException, TestDriverException {
        final var key = TestDriverContextUtils.getKey(handler);
        return serverAction(handler, serverPauseMonitor, "serverPause", (server) -> serverClient.pause(key, server.getId()));
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> serverUnpause(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException, TestDriverException {
        final var key = TestDriverContextUtils.getKey(handler);
        return serverAction(handler, serverUnpauseMonitor, "serverUnpause", (server) -> serverClient.unpause(key, server.getId()));
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> serverShelve(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException, TestDriverException {
        final var key = TestDriverContextUtils.getKey(handler);
        return serverAction(handler, serverShelveMonitor, "serverShelve", (server) -> serverClient.shelve(key, server.getId()));
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> serverUnshelve(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException, TestDriverException {
        final var key = TestDriverContextUtils.getKey(handler);
        return serverAction(handler, serverUnshelveMonitor, "serverUnshelve", (server) -> serverClient.unshelve(key, server.getId()));
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> serverDelete(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException, TestDriverException {
        final var key = TestDriverContextUtils.getKey(handler);
        return serverAction(handler, serverDeleteMonitor, "serverDelete", (server) -> serverClient.delete(server.getId(), key));
    }
}
