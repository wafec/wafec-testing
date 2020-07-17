package wafec.testing.execution.openstack;

public class OpenStackTestDriverExecutionException extends OpenStackTestDriverException {
    public OpenStackTestDriverExecutionException() {
        super();
    }

    public OpenStackTestDriverExecutionException(String message) {
        super(message);
    }

    public OpenStackTestDriverExecutionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
