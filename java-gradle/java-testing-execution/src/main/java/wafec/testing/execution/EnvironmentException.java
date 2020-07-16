package wafec.testing.execution;

public class EnvironmentException extends Exception {
    public EnvironmentException() {
        super();
    }

    public EnvironmentException(String message) {
        super(message);
    }

    public EnvironmentException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
