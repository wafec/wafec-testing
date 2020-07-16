package wafec.testing.execution.app.commandline;

import picocli.CommandLine;
import wafec.testing.execution.helpers.openstack.rabbitmq.RabbitMq;

@CommandLine.Command(name = "helper", subcommands = {
        RabbitMq.class
})
public class TestingHelper {
}
