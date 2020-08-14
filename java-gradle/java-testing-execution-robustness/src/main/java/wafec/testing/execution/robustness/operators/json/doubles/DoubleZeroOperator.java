package wafec.testing.execution.robustness.operators.json.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class DoubleZeroOperator extends AbstractDoubleOperator {
    public static final String NAME = DoubleZeroOperator.class.getSimpleName();

    public DoubleZeroOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Double mutateObject(Double value) throws CouldNotApplyOperatorException {
        return 0.0;
    }
}
