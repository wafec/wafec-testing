package wafec.testing.execution.app;

import picocli.CommandLine;
import wafec.testing.execution.helpers.openstack.rabbitmq.RabbitMq;

@CommandLine.Command(name = "helper", subcommands = {
        RabbitMq.class
})
public class TestingHelper {
}
