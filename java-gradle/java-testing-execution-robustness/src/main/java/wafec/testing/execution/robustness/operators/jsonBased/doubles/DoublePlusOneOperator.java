package wafec.testing.execution.robustness.operators.jsonBased.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class DoublePlusOneOperator extends AbstractDoubleOperator {
    public static final String NAME = DoublePlusOneOperator.class.getSimpleName();

    public DoublePlusOneOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Double mutateObject(Double value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        return value + 1;
    }
}
