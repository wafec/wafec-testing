package wafec.testing.execution;

import java.util.Optional;

public class ConditionWaitStopException extends Exception {
    private ConditionWaitOnErrorResult onErrorResult;

    public ConditionWaitStopException() {
        super();
    }

    public ConditionWaitStopException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ConditionWaitStopException(String message, Throwable throwable, ConditionWaitOnErrorResult onErrorResult) {
        super(message, throwable);
        this.onErrorResult = onErrorResult;
    }

    public boolean exitSuccess() {
        return Optional.ofNullable(onErrorResult).map(ConditionWaitOnErrorResult::isExitSuccess).orElse(false);
    }
}
