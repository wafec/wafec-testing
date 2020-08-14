package wafec.testing.execution.robustness.operators.json.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerPlusOneOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerPlusOneOperator.class.getSimpleName();

    public IntegerPlusOneOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        return value + 1;
    }
}
