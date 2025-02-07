package wafec.testing.execution.robustness.operators.json.booleans;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class BooleanTrueOperator extends AbstractBooleanOperator {
    public static final String NAME = BooleanTrueOperator.class.getSimpleName();

    public BooleanTrueOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Boolean mutateObject(Boolean value) throws CouldNotApplyOperatorException {
        if (value == true)
            throw new CouldNotApplyOperatorException("Value is already true");
        return true;
    }
}
