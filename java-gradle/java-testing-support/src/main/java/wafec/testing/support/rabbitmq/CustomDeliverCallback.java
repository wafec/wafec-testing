package wafec.testing.support.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import wafec.testing.execution.robustness.DataInterceptionException;
import wafec.testing.execution.robustness.DataListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class CustomDeliverCallback implements DeliverCallback {
    private Connection connection;
    private Channel channel;
    private String destinationExchange;
    private DataListener dataListener;

    @Override
    public void handle(String consumerTag, Delivery message) throws IOException {
        byte[] bodyResult = null;
        try {
            var result = dataListener.intercept(message.getBody());
            bodyResult = result.getCurrent().getRaw();
        } catch (DataInterceptionException exc) {
            exc.printStackTrace();
            bodyResult = message.getBody();
        }
        try (Channel channel = connection.createChannel()) {
            channel.basicPublish(destinationExchange, "", message.getProperties(), bodyResult);
        } catch (TimeoutException exc) {
            throw new IOException(exc.getMessage(), exc);
        }
    }
}
