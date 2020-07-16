package wafec.testing.execution.app.commandline.helpers.openstack.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine.*;
import wafec.testing.support.rabbitmq.RabbitMqDataInterception;
import wafec.testing.support.rabbitmq.management.BindingView;
import wafec.testing.support.rabbitmq.management.RabbitMqManagementClientWrapped;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Command(name = "bindings")
public class RabbitMqBindings implements Callable<Integer> {
    @Option(names = {"-di", "--detect-inconsistencies"})
    private boolean detectInconsistencies;

    @Autowired
    private RabbitMqManagementClientWrapped managementClient;

    @Override
    public Integer call() throws Exception {
        System.out.println("-- All Bindings");
        List<BindingView> bindings = new ArrayList<>();
        for (var host : managementClient.getVirtualHosts()) {
            for (var binding : managementClient.getBindings(host.getName())) {
                System.out.println(binding.toString());
                bindings.add(binding);
            }
        }
        if (detectInconsistencies) {
            System.out.println("-- Detecting Inconsistencies...");
            var filterResult = bindings.stream().filter(b -> b.getSource().contains(RabbitMqDataInterception.RDI_PREFIX) ||
                    b.getDestination().contains(RabbitMqDataInterception.RDI_PREFIX)).collect(Collectors.toList());
            if (filterResult.size() > 0) {
                for (var binding : filterResult) {
                    System.out.println(binding.toString());
                }
            } else {
                System.out.println("-- No inconsistencies found");
            }
        }
        return 0;
    }
}
