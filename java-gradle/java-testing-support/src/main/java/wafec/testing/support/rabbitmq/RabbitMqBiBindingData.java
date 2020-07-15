package wafec.testing.support.rabbitmq;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class RabbitMqBiBindingData {
    private String sourceExchange1;
    private String destinationQueue1;
    private String routingKey1;
    private String sourceExchange2;
    private String sourceExchangeType2;
    private String destinationQueue2;
    private String routingKey2;

    public static RabbitMqBiBindingData of(String sourceExchange1,
                                           String destinationQueue1,
                                           String routingKey1,
                                           String sourceExchange2,
                                           String sourceExchangeType2,
                                           String destinationQueue2,
                                           String routingKey2) {
        var instance = new RabbitMqBiBindingData();
        instance.setSourceExchange1(sourceExchange1);
        instance.setDestinationQueue1(destinationQueue1);
        instance.setRoutingKey1(routingKey1);
        instance.setSourceExchange2(sourceExchange2);
        instance.setDestinationQueue2(destinationQueue2);
        instance.setRoutingKey2(routingKey2);
        instance.setSourceExchangeType2(sourceExchangeType2);
        return instance;
    }
}
