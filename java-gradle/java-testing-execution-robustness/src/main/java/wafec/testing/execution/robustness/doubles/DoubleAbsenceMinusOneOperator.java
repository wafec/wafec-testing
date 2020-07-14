package wafec.testing.execution.robustness.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class DoubleAbsenceMinusOneOperator extends AbstractDoubleOperator {
    public static final String NAME = DoubleAbsenceMinusOneOperator.class.getSimpleName();

    public DoubleAbsenceMinusOneOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Double mutateObject(Double value) throws CouldNotApplyOperatorException {
        return 0.0 - 1.0;
    }
}
