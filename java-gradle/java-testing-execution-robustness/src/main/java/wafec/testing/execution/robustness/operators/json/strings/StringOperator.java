package wafec.testing.execution.robustness.operators.json.strings;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.GenericTypeOperator;

public interface StringOperator extends GenericTypeOperator {
    String mutateString(String data) throws CouldNotApplyOperatorException;
}
