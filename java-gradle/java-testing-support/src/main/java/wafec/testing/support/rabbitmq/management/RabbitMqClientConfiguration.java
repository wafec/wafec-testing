package wafec.testing.support.rabbitmq.management;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.auth.BasicAuthRequestInterceptor;

@Configuration
public class RabbitMqClientConfiguration {
    @Value("${wafec.testing.support.rabbitmq.management.client.username}")
    private String username;
    @Value("${wafec.testing.support.rabbitmq.management.client.password}")
    private String password;

    @Bean
    public Encoder getEncoder() {
        return new GsonEncoder();
    }

    @Bean
    public Decoder getDecoder() {
        return new GsonDecoder();
    }

    @Bean
    public ErrorDecoder getErrorDecoder() {
        return new RabbitMqClientErrorDecoder();
    }

    @Bean
    public BasicAuthRequestInterceptor getBasicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }
}
