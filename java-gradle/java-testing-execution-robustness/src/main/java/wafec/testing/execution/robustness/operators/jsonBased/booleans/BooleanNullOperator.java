package wafec.testing.execution.robustness.operators.jsonBased.booleans;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class BooleanNullOperator extends AbstractBooleanOperator {
    public static final String NAME = BooleanNullOperator.class.getSimpleName();

    public BooleanNullOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Boolean mutateObject(Boolean value) throws CouldNotApplyOperatorException {
        if (value == null)
            throw new CouldNotApplyOperatorException("Value is already null");
        return null;
    }
}
