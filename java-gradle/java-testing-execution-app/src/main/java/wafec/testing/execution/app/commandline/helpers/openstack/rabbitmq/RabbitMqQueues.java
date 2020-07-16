package wafec.testing.execution.app.commandline.helpers.openstack.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import wafec.testing.support.rabbitmq.management.RabbitMqManagementClientWrapped;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "queues")
public class RabbitMqQueues implements Callable<Integer> {
    @Autowired
    private RabbitMqManagementClientWrapped managementClient;

    @Override
    public Integer call() throws Exception {
        for(var host : managementClient.getVirtualHosts()) {
            for (var queue : managementClient.getQueues(host.getName())) {
                System.out.println(queue.toString());
            }
        }
        return 0;
    }
}
