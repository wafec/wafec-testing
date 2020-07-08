package wafec.testing.driver.openstack.client;

public class OpenStackClientException extends Exception {
    public OpenStackClientException() {
        super();
    }

    public OpenStackClientException(String message) {
        super(message);
    }

    public OpenStackClientException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
