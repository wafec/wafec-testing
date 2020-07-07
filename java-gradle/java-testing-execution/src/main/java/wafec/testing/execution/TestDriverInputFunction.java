package wafec.testing.execution;

import java.util.List;

public interface TestDriverInputFunction {
    List<TestDriverObservedOutput> apply(TestDriverInputFunctionHandler handler)
            throws TestDataValueNotFoundException, PreConditionViolationException;
}
