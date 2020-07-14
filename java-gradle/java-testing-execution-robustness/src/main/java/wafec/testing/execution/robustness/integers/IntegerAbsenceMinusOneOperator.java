package wafec.testing.execution.robustness.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerAbsenceMinusOneOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerAbsenceMinusOneOperator.class.getSimpleName();

    public IntegerAbsenceMinusOneOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        return 0 - 1;
    }
}
