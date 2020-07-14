package wafec.testing.execution.helpers.openstack.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import wafec.testing.support.rabbitmq.management.RabbitMqManagementClientWrapped;
import wafec.testing.support.rabbitmq.management.VirtualHostView;

import java.util.Optional;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "exchanges")
public class RabbitMqExchanges implements Callable<Integer> {
    @Autowired
    private RabbitMqManagementClientWrapped managementClient;

    @Override
    public Integer call() throws Exception {
        for (var host : managementClient.getVirtualHosts()) {
            for (var exchange : managementClient.getExchanges(Optional.of(host).map(VirtualHostView::getName).get())) {
                System.out.println(exchange.toString());
            }
        }
        return 0;
    }
}
