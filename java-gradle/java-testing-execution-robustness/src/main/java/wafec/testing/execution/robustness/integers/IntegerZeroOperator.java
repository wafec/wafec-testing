package wafec.testing.execution.robustness.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerZeroOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerZeroOperator.class.getSimpleName();

    public IntegerZeroOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        return 0;
    }
}
