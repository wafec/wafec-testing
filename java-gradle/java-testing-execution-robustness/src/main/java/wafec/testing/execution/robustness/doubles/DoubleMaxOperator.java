package wafec.testing.execution.robustness.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class DoubleMaxOperator extends AbstractDoubleOperator {
    public static final String NAME = DoubleMaxOperator.class.getSimpleName();

    public DoubleMaxOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Double mutateObject(Double value) throws CouldNotApplyOperatorException {
        return Double.MAX_VALUE;
    }
}
