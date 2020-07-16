package wafec.testing.execution.robustness.booleans;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.DataTamper;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractBooleanOperator extends JsonBaseOperator<Boolean> implements BooleanOperator {
    public AbstractBooleanOperator() {
        super();
        category = DataTamper.BOOLEAN;
        dataType = Boolean.class.getSimpleName();
    }

    @Override
    public Boolean mutateBoolean(Boolean data) throws CouldNotApplyOperatorException {
        return mutateObject(data);
    }
}
