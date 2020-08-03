package wafec.testing.execution.robustness.operators.jsonBased.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class DoubleZeroMinusOneOperator extends AbstractDoubleOperator {
    public static final String NAME = DoubleZeroMinusOneOperator.class.getSimpleName();

    public DoubleZeroMinusOneOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Double mutateObject(Double value) throws CouldNotApplyOperatorException {
        return 0.0 - 1.0;
    }
}
