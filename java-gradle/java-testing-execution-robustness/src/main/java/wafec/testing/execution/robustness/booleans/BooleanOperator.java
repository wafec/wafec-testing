package wafec.testing.execution.robustness.booleans;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.GenericTypeOperator;

public interface BooleanOperator extends GenericTypeOperator {
    Boolean mutateBoolean(Boolean data) throws CouldNotApplyOperatorException;
}
