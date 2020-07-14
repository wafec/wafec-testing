package wafec.testing.execution.robustness.strings;

import org.apache.commons.lang3.RandomStringUtils;
import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class StringAlphabeticOperator extends AbstractStringOperator {
    public static final String NAME = StringAlphabeticOperator.class.getSimpleName();

    public StringAlphabeticOperator() {
        super();
        name = NAME;
    }

    @Override
    protected String mutateObject(String value) throws CouldNotApplyOperatorException {
        throwIfEmpty(value);
        return RandomStringUtils.randomAlphabetic(value.length());
    }
}
