package wafec.testing.execution.robustness.operators.jsonBased.lists;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.List;

public class ListItemExtraOperator extends AbstractListOperator {
    public static final String NAME = ListItemExtraOperator.class.getSimpleName();

    public ListItemExtraOperator() {
        super();
        name = NAME;
    }

    @Override
    protected List<Object> mutateObject(List<Object> value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        value.add(null);
        return value;
    }
}
