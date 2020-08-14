package wafec.testing.execution.robustness.operators.json.strings;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

public class StringEmptyOperator extends AbstractStringOperator {
    public static final String NAME = StringEmptyOperator.class.getSimpleName();

    public StringEmptyOperator() {
        super();
        name = NAME;
    }

    @Override
    protected String mutateObject(String value) throws CouldNotApplyOperatorException {
        return "";
    }
}
