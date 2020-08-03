package wafec.testing.execution.robustness.operators.jsonBased.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerMaxOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerMaxOperator.class.getSimpleName();

    public IntegerMaxOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        return Integer.MAX_VALUE;
    }
}
