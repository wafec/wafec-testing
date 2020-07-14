package wafec.testing.support.rabbitmq.management;

import feign.Response;
import feign.codec.ErrorDecoder;

public class RabbitMqClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        System.out.println(response.request().url());
        return new Exception(response.reason());
    }
}
