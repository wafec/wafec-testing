package wafec.testing.driver.openstack.client;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomOpenStackClientConfiguration {
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
        return new CustomOpenStackErrorDecoder();
    }
}
