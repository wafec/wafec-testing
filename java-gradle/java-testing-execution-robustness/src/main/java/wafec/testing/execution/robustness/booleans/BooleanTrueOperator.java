package wafec.testing.execution.robustness.booleans;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class BooleanTrueOperator extends AbstractBooleanOperator {
    public static final String NAME = BooleanTrueOperator.class.getSimpleName();

    public BooleanTrueOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Boolean mutateObject(Boolean value) throws CouldNotApplyOperatorException {
        return true;
    }
}
