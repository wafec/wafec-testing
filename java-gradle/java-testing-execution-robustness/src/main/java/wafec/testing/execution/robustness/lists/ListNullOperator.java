package wafec.testing.execution.robustness.lists;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.List;

public class ListNullOperator extends AbstractListOperator {
    public static final String NAME = ListNullOperator.class.getSimpleName();

    public ListNullOperator() {
        super();
        name = NAME;
    }

    @Override
    protected List<Object> mutateObject(List<Object> value) throws CouldNotApplyOperatorException {
        return null;
    }
}
