package wafec.testing.execution.robustness.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerAbsenceOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerAbsenceOperator.class.getSimpleName();

    public IntegerAbsenceOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        return 0;
    }
}
