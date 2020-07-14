package wafec.testing.execution.openstack.robustness;

import org.springframework.stereotype.Component;
import wafec.testing.execution.openstack.OpenStackTestDriver;
import wafec.testing.execution.robustness.AbstractRobustnessTestRunner;
import wafec.testing.execution.robustness.DummyDataCorruption;
import wafec.testing.support.rabbitmq.RabbitMqDataInterception;

@Component
public class OpenStackRobustnessTestRunner extends AbstractRobustnessTestRunner {
    public OpenStackRobustnessTestRunner(OpenStackTestDriver openStackTestDriver,
                                         RabbitMqDataInterception rabbitMqDataInterception,
                                         OpenStackDataHandler openStackDataHandler,
                                         DummyDataCorruption dataCorruption) {
        super(openStackTestDriver, rabbitMqDataInterception, dataCorruption, openStackDataHandler);
    }
}
