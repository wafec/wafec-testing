package wafec.testing.support.rabbitmq;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wafec.testing.execution.robustness.DataListener;

import java.io.IOException;
import java.util.function.Function;

public class CustomDefaultConsumer extends DefaultConsumer {
    private RabbitMqBiBindingData customBinding;
    private CustomDefaultDataTransformer transformer;

    Logger logger = LoggerFactory.getLogger(CustomDefaultConsumer.class);

    public CustomDefaultConsumer(Channel channel, RabbitMqBiBindingData customBinding,
                                 CustomDefaultDataTransformer transformer) {
        super(channel);
        this.customBinding = customBinding;
        this.transformer = transformer;
    }

    public CustomDefaultConsumer(Channel channel, RabbitMqBiBindingData customBinding) {
        this(channel, customBinding, null);
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        long deliveryTag = envelope.getDeliveryTag();

        try {
            if (transformer != null)
                body = transformer.apply(body);
        } catch (DataTransformerException exc) {
            logger.warn(exc.getMessage());
        }

        try {
            getChannel().basicPublish(customBinding.getSourceExchange2(), customBinding.getRoutingKey2(), properties, body);
            getChannel().basicAck(deliveryTag, true);
        } catch (AlreadyClosedException exc) {
            logger.warn(exc.getMessage());
        }
    }
}
