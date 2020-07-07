package wafec.testing.driver.openstack.client;

import feign.codec.Encoder;
import feign.gson.GsonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomOpenStackClientConfiguration {
    @Bean
    public Encoder getEncoder() {
        return new GsonEncoder();
    }
}
