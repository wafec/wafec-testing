package wafec.testing.execution;

import lombok.NoArgsConstructor;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

@NoArgsConstructor
public class ConditionWaiter {
    public static final long DEFAULT_TIMEOUT = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
    public static final long DEFAULT_INTERVAL = 100;

    private long timeout = DEFAULT_TIMEOUT;
    private long interval = DEFAULT_INTERVAL;
    private final List<Class> handledErrorClasses = new ArrayList<>();

    private Function<Exception, ConditionWaitOnErrorResult> onErrorCallback = (e) -> ConditionWaitOnErrorResult.propagatePass(false);

    public ConditionWaiter(long timeout) {
        this(timeout, DEFAULT_INTERVAL);
    }

    public ConditionWaiter(long timeout, long interval) {
        this.timeout = timeout;
        this.interval = interval;
    }

    private void handleException(Exception exc, List<Exception> errors) throws
            Exception {
        if (exc instanceof UndeclaredThrowableException && exc.getCause() != null) {
            if (exc.getCause() instanceof Exception)
                exc = (Exception) exc.getCause();
        }
        errors.add(exc);
        final var caughtExc = exc;
        if (handledErrorClasses.stream().anyMatch(t -> t.equals(caughtExc.getClass())))
            throw exc;
        ConditionWaitOnErrorResult onErrorResult;
        if ((onErrorResult = onErrorCallback.apply(exc)).isPropagate())
            throw new ConditionWaitStopException("Condition wait force stop", exc, onErrorResult);
    }

    private ConditionWaitResult waitForConditionBe(boolean expectedResult, ConditionWaitPredicate predicate) throws
            ConditionWaitStopException, Exception {
        long elapsedTime = 0;
        List<Exception> errors = new ArrayList<>();
        boolean observedResult = !expectedResult;
        try {
            try {
                observedResult = predicate.test();
            } catch (Exception exc) {
                handleException(exc, errors);
            }
            var startTime = new Date();
            var endTime = startTime;
            elapsedTime = (endTime.getTime() - startTime.getTime());
            while (observedResult != expectedResult && elapsedTime < timeout) {
                Thread.sleep(interval);
                try {
                    observedResult = predicate.test();
                } catch (Exception exc) {
                    handleException(exc, errors);
                }
                endTime = new Date();
                elapsedTime = (endTime.getTime() - startTime.getTime());
            }
            if (observedResult == expectedResult) {
                var validResult = new ConditionWaitResult();
                validResult.setElapsedTime(elapsedTime);
                validResult.setExitSuccess(true);
                validResult.setErrors(errors);
                return validResult;
            } else {
                var invalidResult = new ConditionWaitResult();
                invalidResult.setElapsedTime(elapsedTime);
                invalidResult.setExitSuccess(false);
                invalidResult.setErrors(errors);
                return invalidResult;
            }
        } catch (InterruptedException intExc) {
            var errorResult = new ConditionWaitResult();
            errorResult.setElapsedTime(elapsedTime);
            errorResult.setExitSuccess(false);
            errors.add(intExc);
            errorResult.setErrors(errors);
            return errorResult;
        }
    }

    public ConditionWaitResult waitForConditionBeTrue(ConditionWaitPredicate predicate) throws
            ConditionWaitStopException, Exception {
        return waitForConditionBe(true, predicate);
    }

    public ConditionWaitResult waitForConditionBeFalse(ConditionWaitPredicate predicate) throws
            ConditionWaitStopException, Exception {
        return waitForConditionBe(false, predicate);
    }

    public ConditionWaiter handle(Class errorType) {
        handledErrorClasses.add(errorType);
        return this;
    }

    public ConditionWaiter onError(Function<Exception, ConditionWaitOnErrorResult> predicate) {
        this.onErrorCallback = predicate;
        return this;
    }
}
