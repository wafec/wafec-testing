package wafec.testing.execution.robustness.booleans;

import org.apache.commons.lang3.StringUtils;
import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class BooleanNegateOperator extends AbstractBooleanOperator {
    public static final String NAME = BooleanNegateOperator.class.getSimpleName();

    public BooleanNegateOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Boolean mutateObject(Boolean value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        return !value;
    }
}
