package wafec.testing.execution.robustness.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class DoubleAbsenceOperator extends AbstractDoubleOperator {
    public static final String NAME = DoubleAbsenceOperator.class.getSimpleName();

    public DoubleAbsenceOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Double mutateObject(Double value) throws CouldNotApplyOperatorException {
        return 0.0;
    }
}
