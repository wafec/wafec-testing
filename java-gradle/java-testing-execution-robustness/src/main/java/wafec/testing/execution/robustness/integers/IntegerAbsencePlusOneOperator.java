package wafec.testing.execution.robustness.integers;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class IntegerAbsencePlusOneOperator extends AbstractIntegerOperator {
    public static final String NAME = IntegerAbsencePlusOneOperator.class.getSimpleName();

    public IntegerAbsencePlusOneOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Integer mutateObject(Integer value) throws CouldNotApplyOperatorException {
        return 0 + 1;
    }
}
