package wafec.testing.support.rabbitmq.management;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitMqManagementClientWrapped {
    @Autowired
    private RabbitMqManagementClient managementClient;

    private boolean isDefaultHost(String virtualHost) {
        return virtualHost == null || virtualHost.equals("/") || virtualHost.equals("%2F") || StringUtils.isEmpty(virtualHost);
    }

    public List<VirtualHostView> getVirtualHosts() {
        return managementClient.getVirtualHosts();
    }

    public List<BindingView> getBindings(String virtualHost) {
        if (isDefaultHost(virtualHost))
            return managementClient.getBindings();
        return managementClient.getBindings(virtualHost);
    }

    public ExchangeView getExchange(String virtualHost, String exchangeName) {
        if (isDefaultHost(exchangeName))
            return null;
        if (isDefaultHost(virtualHost))
            return managementClient.getExchange(exchangeName);
        return managementClient.getExchange(virtualHost, exchangeName);
    }

    public List<QueueView> getQueues(String virtualHost) {
        if (isDefaultHost(virtualHost))
            return managementClient.getQueues();
        return managementClient.getQueues(virtualHost);
    }

    public List<ExchangeView> getExchanges(String virtualHost) {
        if (isDefaultHost(virtualHost))
            return managementClient.getExchanges();
        return managementClient.getExchanges(virtualHost);
    }

    public QueueView getQueue(String virtualHost, String queueName) {
        if (isDefaultHost(queueName))
            return null;
        if (isDefaultHost(virtualHost))
            return managementClient.getQueue(queueName);
        return managementClient.getQueue(virtualHost, queueName);
    }
}
