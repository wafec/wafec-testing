package wafec.testing.execution.helpers.openstack.rabbitmq;

import picocli.CommandLine.*;

@Command(name = "rabbitmq", subcommands = {
        RabbitMqBindings.class,
        RabbitMqQueues.class,
        RabbitMqExchanges.class
})
public class RabbitMq {
}
