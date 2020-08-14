package wafec.testing.execution.robustness.operators.json.booleans;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class BooleanNegateOperator extends AbstractBooleanOperator {
    public static final String NAME = BooleanNegateOperator.class.getSimpleName();

    public BooleanNegateOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Boolean mutateObject(Boolean value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        return !value;
    }
}
