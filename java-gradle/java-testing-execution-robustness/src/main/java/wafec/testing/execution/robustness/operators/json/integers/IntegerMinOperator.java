package wafec.testing.execution.robustness.operators.json.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerMinOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerMinOperator.class.getSimpleName();

    public IntegerMinOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        return Integer.MIN_VALUE;
    }
}
