package wafec.testing.execution.robustness;

public class DataInterceptionException extends RobustnessException {
    public DataInterceptionException() {
        super();
    }

    public DataInterceptionException(String message) {
        super(message);
    }

    public DataInterceptionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
