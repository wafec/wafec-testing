package wafec.testing.execution.robustness;

public class UnexpectedInjectorException extends RobustnessException {
    public UnexpectedInjectorException() {
        super();
    }

    public UnexpectedInjectorException(String message) {
        super(message);
    }

    public UnexpectedInjectorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
