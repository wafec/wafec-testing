package wafec.testing.driver.openstack.client;

import lombok.Getter;

public class OpenStackClientBadRequestException extends OpenStackClientException {
    @Getter
    private int statusCode;

    public OpenStackClientBadRequestException(int statusCode) {
        this.statusCode = statusCode;
    }

    public OpenStackClientBadRequestException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public OpenStackClientBadRequestException(int statusCode, String message, Throwable throwable) {
        super(message, throwable);
        this.statusCode = statusCode;
    }
}
