package wafec.testing.execution.robustness.operators.json.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.DataTamper;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractIntegerOperator extends JsonBaseOperator<Integer> implements IntegerOperator {
    public AbstractIntegerOperator() {
        super();
        category = DataTamper.NUMBER;
        dataType = Integer.class.getSimpleName();
    }

    @Override
    public Integer mutateInteger(Integer data) throws CouldNotApplyOperatorException {
        return mutateObject(data);
    }
}
