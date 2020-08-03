package wafec.testing.execution.robustness.operators.jsonBased.strings;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class StringNullOperator extends AbstractStringOperator {
    public static final String NAME = StringNullOperator.class.getSimpleName();

    public StringNullOperator() {
        super();
        name = NAME;
    }

    @Override
    protected String mutateObject(String value) throws CouldNotApplyOperatorException {
        return null;
    }
}
