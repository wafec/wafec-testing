package wafec.testing.support.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wafec.testing.execution.robustness.DataInterception;
import wafec.testing.execution.robustness.DataInterceptionException;
import wafec.testing.execution.robustness.DataListener;
import wafec.testing.support.rabbitmq.management.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitMqDataInterception implements DataInterception {
    private static final String RDI_PREFIX = "RDI-";
    private static final String RDI_QUEUE_PREFIX = String.format("%sQ-", RDI_PREFIX);
    private static final String RDI_EXCHANGE_PREFIX = String.format("%sE-", RDI_PREFIX);

    private DataListener dataCallback;

    @Value("${wafec.testing.support.rabbitmq.connection.host}")
    private String host;
    @Value("${wafec.testing.support.rabbitmq.connection.username}")
    private String username;
    @Value("${wafec.testing.support.rabbitmq.connection.password}")
    private String password;
    @Value("${wafec.testing.support.rabbitmq.connection.port}")
    private int port;

    @Autowired
    private RabbitMqManagementClientWrapped rabbitManagementClient;

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private List<RabbitMqCustomBinding> customBindings;

    Logger logger = LoggerFactory.getLogger(RabbitMqDataInterception.class);

    private void ensureConnection() throws IOException, TimeoutException {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        connection = connectionFactory.newConnection();
    }

    @Override
    public void turnOn(DataListener dataCallback) throws DataInterceptionException {
        try {
            this.dataCallback = dataCallback;
            this.customBindings = new ArrayList<>();
            ensureConnection();
            var hosts = rabbitManagementClient.getVirtualHosts();
            for (var host : hosts) {
                logger.debug(String.format("Host: %s", host));
                var bindings = this.rabbitManagementClient.getBindings(Optional.of(host).map(VirtualHostView::getName).get());
                for (var binding : bindings) {
                    intercept(binding);
                }
            }
        } catch (IOException | TimeoutException exc) {
            exc.printStackTrace();
            throw new DataInterceptionException(exc.getMessage(), exc);
        }
    }

    private void intercept(BindingView binding) throws IOException, TimeoutException {
        if (Optional.of(binding).map(BindingView::getDestinationType)
                .map(String::toLowerCase).equals(Optional.of("queue")) &&
            !Optional.of(binding).map(BindingView::getSource).equals(Optional.of("/"))) {
            var exchangeName = binding.getSource();
            var exchange = rabbitManagementClient.getExchange(binding.getVirtualHost(), exchangeName);
            if (exchange == null)
                return;
            if (Optional.of(exchange).map(ExchangeView::getExchangeType).map(String::toLowerCase)
                .equals(Optional.of("topic"))) {
                interceptTopic(binding);
            }
        }
    }

    private void interceptTopic(BindingView binding) throws IOException, TimeoutException {
            Channel channel = connection.createChannel();
            var rdiQueue = String.format("%s%s", RDI_QUEUE_PREFIX, binding.getDestination());
            var rdiExchange = String.format("%s%s", RDI_EXCHANGE_PREFIX, binding.getSource());

            customBindings.add(RabbitMqCustomBinding.of(binding.getSource(), rdiExchange, binding.getRoutingKey(),
                    rdiExchange, binding.getDestination(), ""));

            QueueView queueView = rabbitManagementClient.getQueue(binding.getVirtualHost(), binding.getDestination());
            channel.queueUnbind(binding.getDestination(), binding.getSource(), binding.getRoutingKey());
            channel.queueDeclare(rdiQueue, queueView.isDurable(), queueView.isExclusive(),
                    queueView.isAutoDelete(), null);
            channel.exchangeDeclare(rdiExchange, "direct");
            channel.queueBind(binding.getDestination(), rdiExchange, "");
            channel.queueBind(rdiQueue, binding.getSource(), binding.getRoutingKey());
            channel.basicConsume(rdiQueue, true, CustomDeliverCallback.of(connection, channel, rdiExchange, this.dataCallback), (tag) -> {
            });
    }

    private void tryRestoreBindings() {
        for(var customBinding : customBindings) {
            try (Channel channel = connection.createChannel()) {
                channel.queueUnbind(customBinding.getSourceExchange1(), customBinding.getDestinationQueue1(),
                        customBinding.getRoutingKey1());
                channel.queueUnbind(customBinding.getSourceExchange2(), customBinding.getDestinationQueue2(),
                        customBinding.getRoutingKey2());
                channel.queueDelete(customBinding.getDestinationQueue1());
                channel.exchangeDelete(customBinding.getSourceExchange2());
                channel.queueBind(customBinding.getDestinationQueue2(), customBinding.getSourceExchange1(),
                        customBinding.getRoutingKey1());
            } catch(IOException | TimeoutException exc) {
                logger.warn(exc.getMessage(), exc);
            }
        }
        customBindings = null;
    }

    @Override
    public void turnOff() throws DataInterceptionException {
        this.dataCallback = null;
        try {
            if (customBindings != null && customBindings.size() > 0)
                tryRestoreBindings();
            if (connection != null)
                connection.close();
            System.out.println("Closed");
        } catch (IOException exc) {
            throw new DataInterceptionException(exc.getMessage(), exc);
        }
    }
}
