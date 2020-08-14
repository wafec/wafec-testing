package wafec.testing.execution.robustness.operators.json.booleans;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class BooleanFalseOperator extends AbstractBooleanOperator {
    public static final String NAME = BooleanFalseOperator.class.getSimpleName();

    public BooleanFalseOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Boolean mutateObject(Boolean value) throws CouldNotApplyOperatorException {
        if (value == false)
            throw new CouldNotApplyOperatorException("Value is already false");
        return false;
    }
}
