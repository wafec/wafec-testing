package wafec.testing.execution.robustness.operators.json.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerNullOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerNullOperator.class.getSimpleName();

    public IntegerNullOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        return null;
    }
}
