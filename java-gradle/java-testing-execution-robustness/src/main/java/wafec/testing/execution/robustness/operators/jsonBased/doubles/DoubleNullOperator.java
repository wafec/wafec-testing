package wafec.testing.execution.robustness.operators.jsonBased.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class DoubleNullOperator extends AbstractDoubleOperator {
    public static final String NAME = DoubleNullOperator.class.getSimpleName();

    public DoubleNullOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Double mutateObject(Double value) throws CouldNotApplyOperatorException {
        return null;
    }
}
