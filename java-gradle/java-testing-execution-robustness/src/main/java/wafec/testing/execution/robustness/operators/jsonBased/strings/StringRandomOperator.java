package wafec.testing.execution.robustness.operators.jsonBased.strings;

import org.apache.commons.lang3.RandomStringUtils;
import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class StringRandomOperator extends AbstractStringOperator {
    public static final String NAME = StringRandomOperator.class.getSimpleName();

    public StringRandomOperator() {
        super();
        name = NAME;
    }

    @Override
    protected String mutateObject(String value) throws CouldNotApplyOperatorException {
        throwIfEmpty(value);
        return RandomStringUtils.random(value.length());
    }
}
