package wafec.testing.execution.robustness.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerZeroMinusOneOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerZeroMinusOneOperator.class.getSimpleName();

    public IntegerZeroMinusOneOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        return 0 - 1;
    }
}
