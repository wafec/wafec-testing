package wafec.testing.execution.openstack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.driver.openstack.client.*;
import wafec.testing.execution.*;
import wafec.testing.execution.openstack.modules.NovaInputModelBuilderModule;

import java.util.List;

@Component
public class OpenStackTestDriver extends AbstractTestDriverAdapter {
    public static final String SECURED = "preConditionSecured";

    @Autowired
    private KeyRepository keyRepository;

    public OpenStackTestDriver(final NovaInputModelBuilderModule novaBuilderModule) {
        super();
        configurePost((builder) -> {
            builder.map(novaBuilderModule);
        });
    }

    @Override
    protected void configure(TestDriverInputModelBuilder builder) {
        builder
                .map(SECURED, this::preConditionSecured)
                .map("keyCreate", this::keyCreate)
        ;
    }

    private List<TestDriverObservedOutput> preConditionSecured(TestDriverInputFunctionHandler handler)
            throws PreConditionViolationException {
        var key = handler.getTestDriverContext().get("key");
        if (key.isEmpty())
            throw new PreConditionViolationException("Key cannot be null");
        return TestDriverObservedOutput.success("preCondition").asList();
    }

    private void removeKey(long keyId) {
        var key = keyRepository.findById(keyId);
        key.ifPresent(value -> keyRepository.delete(value));
    }

    private List<TestDriverObservedOutput> keyCreate(TestDriverInputFunctionHandler handler)
            throws TestDataValueNotFoundException {
        var testExecution = handler.getTestExecution();
        var testData = handler.getTestData();

        removeKey(testExecution.getId());

        var key = new Key();
        key.setId(testExecution.getId());
        key.setUsername(testData.get("username").map(TestDataValue::getValue).get());
        key.setPassword(testData.get("password").map(TestDataValue::getValue).get());
        key.setAuthUrl(testData.get("auth_url").map(TestDataValue::getValue).get());
        key.setProjectDomainName(testData.get("project_domain_name").map(TestDataValue::getValue).get());
        key.setProjectName(testData.get("project_name").map(TestDataValue::getValue).get());
        key.setUserDomainName(testData.get("user_domain_name").map(TestDataValue::getValue).get());
        keyRepository.save(key);

        TestDriverContextUtils.setKey(handler, key.getId());

        return TestDriverObservedOutput.none().asList();
    }
}
