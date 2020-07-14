package wafec.testing.support.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class RabbitMqCustomBinding {
    private String sourceExchange1;
    private String destinationQueue1;
    private String routingKey1;
    private String sourceExchange2;
    private String destinationQueue2;
    private String routingKey2;
}
