package wafec.testing.execution;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConditionWaitOnErrorResult {
    private boolean propagate;
    private boolean exitSuccess;

    public static ConditionWaitOnErrorResult propagatePass(boolean propagate) {
        ConditionWaitOnErrorResult result = new ConditionWaitOnErrorResult();
        result.propagate = propagate;
        result.exitSuccess = true;
        return result;
    }

    public static ConditionWaitOnErrorResult propagateFail(boolean propagate) {
        var result = propagatePass(propagate);
        result.exitSuccess = false;
        return result;
    }
}
