package wafec.testing.execution.robustness.operators.json.strings;

import org.apache.commons.lang3.RandomStringUtils;
import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class StringOverflowOperator extends AbstractStringOperator {
    public static final String NAME = StringOverflowOperator.class.getSimpleName();
    public StringOverflowOperator() {
        super();
        name = NAME;
    }

    @Override
    protected String mutateObject(String value) throws CouldNotApplyOperatorException {
        return RandomStringUtils.random(Short.MAX_VALUE * 2);
    }
}
