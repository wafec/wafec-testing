package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

@NoArgsConstructor
@AllArgsConstructor
public class ConditionWaiter {
    private long timeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
    private long interval = 100;
    private final List<Class> handledErrorClasses = new ArrayList<>();

    public ConditionWaiter(long timeout) {
        this(timeout, 100);
    }

    private ConditionWaitResult waitForConditionBe(boolean expectedResult, BooleanSupplier predicate) {
        long elapsedTime = 0;
        List<Exception> errors = new ArrayList<>();
        boolean observedResult = expectedResult;
        try {
            try {
                observedResult = predicate.getAsBoolean();
            } catch (Exception exc) {
                errors.add(exc);
                if (handledErrorClasses.stream().anyMatch(t -> t.equals(exc.getClass())))
                    throw exc;
            }
            var startTime = new Date();
            var endTime = startTime;
            elapsedTime = (endTime.getTime() - startTime.getTime());
            while (observedResult != expectedResult && elapsedTime < timeout) {
                Thread.sleep(interval);
                try {
                    observedResult = predicate.getAsBoolean();
                } catch (Exception exc) {
                    errors.add(exc);
                    if (handledErrorClasses.stream().anyMatch(t -> t.equals(exc.getClass())))
                        throw exc;
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

    public ConditionWaitResult waitForConditionBeTrue(BooleanSupplier predicate) {
        return waitForConditionBe(true, predicate);
    }

    public ConditionWaitResult waitForConditionBeFalse(BooleanSupplier predicate) {
        return waitForConditionBe(false, predicate);
    }

    public ConditionWaiter handle(Class errorType) {
        handledErrorClasses.add(errorType);
        return this;
    }
}
