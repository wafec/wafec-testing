package wafec.testing.execution.robustness.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class DoubleMinOperator extends AbstractDoubleOperator {
    public static final String NAME = DoubleMinOperator.class.getSimpleName();

    public DoubleMinOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Double mutateObject(Double value) throws CouldNotApplyOperatorException {
        return Double.MIN_VALUE;
    }
}
