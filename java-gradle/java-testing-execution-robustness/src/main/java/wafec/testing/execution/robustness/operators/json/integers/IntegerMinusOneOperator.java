package wafec.testing.execution.robustness.operators.json.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerMinusOneOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerMinusOneOperator.class.getSimpleName();

    public IntegerMinusOneOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        return value - 1;
    }
}
