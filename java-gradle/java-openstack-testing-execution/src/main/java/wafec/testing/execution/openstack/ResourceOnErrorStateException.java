package wafec.testing.execution.openstack;

public class ResourceOnErrorStateException extends OpenStackTestDriverExecutionException {
    public ResourceOnErrorStateException() {
        super();
    }

    public ResourceOnErrorStateException(String message) {
        super(message);
    }

    public ResourceOnErrorStateException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
