package wafec.testing.execution.robustness.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.GenericTypeOperator;

public interface IntegerOperator extends GenericTypeOperator {
    Integer mutateInteger(Integer data) throws CouldNotApplyOperatorException;
}
