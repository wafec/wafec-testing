package wafec.testing.execution.robustness.operators.jsonBased.strings;

import org.apache.commons.lang3.RandomStringUtils;
import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class StringNumericOperator extends AbstractStringOperator {
    public static final String NAME = StringNumericOperator.class.getSimpleName();

    public StringNumericOperator() {
        super();
        name = NAME;
    }

    @Override
    protected String mutateObject(String value) throws CouldNotApplyOperatorException {
        throwIfEmpty(value);
        return RandomStringUtils.randomNumeric(value.length());
    }
}
