package wafec.testing.execution.openstack;

import wafec.testing.execution.TestDriverException;

public class OpenStackTestDriverException extends TestDriverException {
    public OpenStackTestDriverException() {
        super();
    }

    public OpenStackTestDriverException(String message) {
        super(message);
    }

    public OpenStackTestDriverException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
