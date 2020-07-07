package wafec.testing.execution.openstack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.*;
import wafec.testing.execution.*;

import java.util.List;

@Component
public class OpenStackTestDriver extends AbstractTestDriverAdapter {
    private static final String SECURED = "preConditionSecured";

    @Autowired
    private FlavorClient flavorClient;
    @Autowired
    private ImageClient imageClient;
    @Autowired
    private ServerClient serverClient;
    @Autowired
    private VolumeClient volumeClient;
    @Autowired
    private KeyRepository keyRepository;
    private Key key;

    @Override
    protected void configure(TestDriverInputModelBuilder builder) {
        builder
                .map(SECURED, this::preConditionSecured)
                .map("keyCreate", this::keyCreate)
                .map("flavorCreate", this::flavorCreate);
    }

    private List<TestDriverObservedOutput> preConditionSecured(TestDriverInputFunctionHandler handler)
            throws PreConditionViolationException {
        if (key == null)
            throw new PreConditionViolationException("Key cannot be null");
        return TestDriverObservedOutput.success("preCondition").asList();
    }

    private List<TestDriverObservedOutput> keyCreate(TestDriverInputFunctionHandler handler)
            throws TestDataValueNotFoundException {
        var testExecution = handler.getTestExecution();
        var testData = handler.getTestData();
        key = new Key();
        key.setId(testExecution.getId());
        key.setUsername(testData.get("username").map(TestDataValue::getValue).get());
        key.setPassword(testData.get("password").map(TestDataValue::getValue).get());
        key.setAuthUrl(testData.get("auth_url").map(TestDataValue::getValue).get());
        key.setProjectDomainName(testData.get("project_domain_name").map(TestDataValue::getValue).get());
        key.setProjectName(testData.get("project_name").map(TestDataValue::getValue).get());
        key.setUserDomainName(testData.get("user_domain_name").map(TestDataValue::getValue).get());
        keyRepository.save(key);
        return TestDriverObservedOutput.none().asList();
    }

    @PreCondition(target = SECURED)
    private List<TestDriverObservedOutput> flavorCreate(TestDriverInputFunctionHandler handler)
            throws TestDataValueNotFoundException {
        var testData = handler.getTestData();
        Flavor flavor = Flavor.of(testData.get("name").map(TestDataValue::getValue).get(),
                testData.get("ram").map(TestDataValue::getValue).map(Integer::parseInt).get(),
                testData.get("vcpus").map(TestDataValue::getValue).map(Integer::parseInt).get(),
                testData.get("disk").map(TestDataValue::getValue).map(Integer::parseInt).get());
        var flavorResult = flavorClient.create(key.getBase64Id(), flavor);
        return TestDriverObservedOutput.success("flavorCreate").asList();
    }
}
