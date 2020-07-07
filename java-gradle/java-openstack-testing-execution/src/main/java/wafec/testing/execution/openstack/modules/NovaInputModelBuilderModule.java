package wafec.testing.execution.openstack.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.*;
import wafec.testing.execution.*;
import wafec.testing.execution.openstack.OpenStackTestDriver;
import wafec.testing.execution.openstack.ResourceNotCreatedException;
import wafec.testing.execution.openstack.ResourceNotFoundException;
import wafec.testing.execution.openstack.TestDriverContextUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

@Component
public class NovaInputModelBuilderModule extends TestDriverInputModelBuilderModule {
    @Autowired
    private FlavorClient flavorClient;
    @Autowired
    private ImageClient imageClient;
    @Autowired
    private NetworkClient networkClient;
    @Autowired
    private ServerClient serverClient;

    @Override
    protected void configure(TestDriverInputModelBuilder builder) {
        builder
                .map("flavorCreate", this::flavorCreate)
                .map("imageCreate", this::imageCreate)
                .map("serverCreate", this::serverCreate);
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> flavorCreate(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException {
        var testData = handler.getTestData();
        var key = TestDriverContextUtils.getKey(handler);
        Flavor data = new Flavor();
        data.setName(testData.getValue("name"));
        data.setRam(testData.getValueOpt("ram").map(Integer::parseInt).get());
        data.setVcpus(testData.getValueOpt("vcpus").map(Integer::parseInt).get());
        data.setDisk(testData.getValueOpt("disk").map(Integer::parseInt).get());
        var result = flavorClient.create(key, data);
        if (result == null)
            throw new ResourceNotCreatedException(String.format("flavor %s", testData.getValue("name")));
        return TestDriverObservedOutput.success("flavorCreate").asList();
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> imageCreate(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException {
        var testData = handler.getTestData();
        var key = TestDriverContextUtils.getKey(handler);
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
            throw new ResourceNotCreatedException(String.format("image %s", testData.getValue("name")));
        return TestDriverObservedOutput.success("imageCreated").asList();
    }

    @PreCondition(target = OpenStackTestDriver.SECURED)
    private List<TestDriverObservedOutput> serverCreate(TestDriverInputFunctionHandler handler) throws
            TestDataValueNotFoundException {
        var testData = handler.getTestData();
        var key = TestDriverContextUtils.getKey(handler);
        var flavorResult = flavorClient.findByName(key, testData.getValue("flavor"));
        var imageResult = imageClient.findByName(key, testData.getValue("image"));
        var networkResult = networkClient.findByName(key, testData.getValue("network"));
        if (flavorResult == null || imageResult == null || networkResult == null ||
                Stream.of(flavorResult, imageResult, networkResult).anyMatch(list -> list.size() == 0)) {
            throw new ResourceNotFoundException(String.format("(flavor %s | image %s | network %s)",
                    testData.getValue("flavor"), testData.getValue("image"), testData.getValue("network")));
        }
        Server data = new Server();
        data.setName(testData.getValue("name"));
        data.setFlavor(flavorResult.get(0).getId());
        data.setImage(imageResult.get(0).getId());
        data.setNetwork(networkResult.get(0).getId());
        var result = serverClient.create(key, data);
        if (result == null)
            throw new ResourceNotCreatedException(String.format("server %s", testData.getValue("name")));
        return TestDriverObservedOutput.success("serverCreated").asList();
    }
}
