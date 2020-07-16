package wafec.testing.execution.robustness.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerZeroPlusOneOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerZeroPlusOneOperator.class.getSimpleName();

    public IntegerZeroPlusOneOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        return 0 + 1;
    }
}
