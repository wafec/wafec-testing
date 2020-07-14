package wafec.testing.execution.robustness.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class DoubleMinusOneOperator extends AbstractDoubleOperator {
    public static final String NAME = DoubleMinusOneOperator.class.getSimpleName();

    public DoubleMinusOneOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Double mutateObject(Double value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        return value - 1;
    }
}
