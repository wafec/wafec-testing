package wafec.testing.execution.helpers.openstack.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine.*;
import wafec.testing.support.rabbitmq.management.RabbitMqManagementClient;
import wafec.testing.support.rabbitmq.management.RabbitMqManagementClientWrapped;
import wafec.testing.support.rabbitmq.management.VirtualHostView;

import java.util.Optional;
import java.util.concurrent.Callable;

@Command(name = "bindings")
public class RabbitMqBindings implements Callable<Integer> {
    @Autowired
    private RabbitMqManagementClientWrapped managementClient;

    @Override
    public Integer call() throws Exception {
        for (var host : managementClient.getVirtualHosts()) {
            for (var binding : managementClient.getBindings(host.getName())) {
                System.out.println(binding.toString());
            }
        }
        return 0;
    }
}
