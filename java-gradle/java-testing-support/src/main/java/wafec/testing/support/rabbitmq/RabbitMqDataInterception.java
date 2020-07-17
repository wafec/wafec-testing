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
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitMqDataInterception implements DataInterception {
    public static final String RDI_PREFIX = "RDI-";
    private static final String RDI_QUEUE_PREFIX = String.format("%sQ-", RDI_PREFIX);
    private static final String RDI_EXCHANGE_PREFIX = String.format("%sE-", RDI_PREFIX);

    private DataListener dataListener;

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
    private List<RabbitMqBiBindingData> customBindings;

    Logger logger = LoggerFactory.getLogger(RabbitMqDataInterception.class);

    private void connect() throws IOException, TimeoutException {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        connectionFactory.setConnectionTimeout((int)TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS));
        connection = null;
        int i = 0;
        while (connection == null && i < 10) {
            try {
                Thread.sleep(500);
                connection = connectionFactory.newConnection();
            } catch (ConnectException | TimeoutException | InterruptedException exc) {
                logger.warn(exc.getMessage());
            } finally {
                i++;
            }
        }
        if (connection == null)
            throw new TimeoutException("Could not connect");
    }

    private void addCustomBinding(RabbitMqBiBindingData customBinding) {
        customBindings.add(customBinding);
    }

    private void clear() {
        customBindings = null;
        this.dataListener = null;
    }

    @Override
    public void turnOn(DataListener dataListener) throws DataInterceptionException {
        try {
            this.dataListener = dataListener;
            this.customBindings = new ArrayList<>();
            connect();
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
        var customBinding = RabbitMqBiBindingData.of(binding.getSource(), rdiQueue, binding.getRoutingKey(),
                rdiExchange, "topic", binding.getDestination(), binding.getRoutingKey());
        addCustomBinding(customBinding);
        rebindStart(channel, customBinding);
        channel.basicConsume(rdiQueue, false, "rabbitmq-data-interception",
                new CustomDefaultConsumer(channel, customBinding, this::handleDataIntercepted));
    }

    private byte[] handleDataIntercepted(byte[] body) throws DataTransformerException {
        if (dataListener == null)
            throw new DataTransformerException("Interception has closed");

        try {
            var appData = dataListener.intercept(body);
            return appData.getCurrent().getData();
        } catch(DataInterceptionException exc) {
            exc.printStackTrace();
            return body;
        }
    }

    private void rebindStart(Channel channel, RabbitMqBiBindingData bindingData) throws IOException {
        channel.queueDeclare(bindingData.getDestinationQueue1(), true, false, false, null);
        channel.exchangeDeclare(bindingData.getSourceExchange2(), bindingData.getSourceExchangeType2());
        channel.queueBind(bindingData.getDestinationQueue1(), bindingData.getSourceExchange1(), bindingData.getRoutingKey1());
        channel.queueBind(bindingData.getDestinationQueue2(), bindingData.getSourceExchange2(), bindingData.getRoutingKey2());
        channel.queueUnbind(bindingData.getDestinationQueue2(), bindingData.getSourceExchange1(), bindingData.getRoutingKey1());
    }

    private void rebindEnd(Channel channel, RabbitMqBiBindingData bindingData) throws IOException {
        channel.queueBind(bindingData.getDestinationQueue2(), bindingData.getSourceExchange1(), bindingData.getRoutingKey1());
        channel.queueUnbind(bindingData.getDestinationQueue1(), bindingData.getSourceExchange1(), bindingData.getRoutingKey1());
        channel.queueUnbind(bindingData.getDestinationQueue2(), bindingData.getSourceExchange2(), bindingData.getRoutingKey2());
        channel.queueDelete(bindingData.getDestinationQueue1());
        channel.exchangeDelete(bindingData.getSourceExchange2());
    }

    private void tryRestore() {
        for(var customBinding : customBindings) {
            try (Channel channel = connection.createChannel()) {
                rebindEnd(channel, customBinding);
            } catch(IOException | TimeoutException exc) {
                logger.warn(exc.getMessage(), exc);
            }
        }
    }

    @Override
    public void turnOff() throws DataInterceptionException {
        try {
            if (customBindings != null && customBindings.size() > 0)
                tryRestore();
            if (connection != null)
                connection.close();
            clear();
        } catch (IOException exc) {
            throw new DataInterceptionException(exc.getMessage(), exc);
        }
    }
}
