package wafec.testing.driver.openstack.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomOpenStackErrorDecoder implements ErrorDecoder {
    Logger logger = LoggerFactory.getLogger(CustomOpenStackErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() < 500) {
            return new OpenStackClientBadRequestException(response.status(), response.reason());
        }
        return new OpenStackClientException("INTERNAL_SERVER_ERROR");
    }
}
