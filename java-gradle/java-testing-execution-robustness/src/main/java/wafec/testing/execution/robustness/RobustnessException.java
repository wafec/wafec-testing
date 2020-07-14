package wafec.testing.execution.robustness;

public class RobustnessException extends Exception {
    public RobustnessException() {
         super();
    }

    public RobustnessException(String message) {
        super(message);
    }

    public RobustnessException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
