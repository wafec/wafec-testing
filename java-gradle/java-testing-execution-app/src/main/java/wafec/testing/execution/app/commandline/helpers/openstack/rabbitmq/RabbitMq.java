package wafec.testing.execution.app.commandline.helpers.openstack.rabbitmq;

import picocli.CommandLine.*;

@Command(name = "rabbitmq", subcommands = {
        RabbitMqBindings.class,
        RabbitMqQueues.class,
        RabbitMqExchanges.class
})
public class RabbitMq {
}
