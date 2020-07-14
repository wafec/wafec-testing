package wafec.testing.execution.robustness;

public class CouldNotApplyOperatorException extends RobustnessException {
    public CouldNotApplyOperatorException() {
        super();
    }

    public CouldNotApplyOperatorException(String message) {
        super(message);
    }

    public CouldNotApplyOperatorException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
