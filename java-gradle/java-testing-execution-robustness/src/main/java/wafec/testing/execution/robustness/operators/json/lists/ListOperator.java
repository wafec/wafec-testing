package wafec.testing.execution.robustness.operators.json.lists;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.GenericTypeOperator;

import java.util.List;

public interface ListOperator extends GenericTypeOperator {
    List<Object> mutateList(List<Object> data) throws CouldNotApplyOperatorException;
}
