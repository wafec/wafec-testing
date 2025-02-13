package wafec.testing.execution.robustness.operators.json.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.GenericTypeOperator;

public interface DoubleOperator extends GenericTypeOperator {
    Double mutateDouble(Double data) throws CouldNotApplyOperatorException;
}
