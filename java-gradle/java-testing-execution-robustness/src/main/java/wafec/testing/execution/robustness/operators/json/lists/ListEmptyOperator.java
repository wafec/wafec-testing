package wafec.testing.execution.robustness.operators.json.lists;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.ArrayList;
import java.util.List;

public class ListEmptyOperator extends AbstractListOperator {
    public static final String NAME = ListEmptyOperator.class.getSimpleName();

    public ListEmptyOperator() {
        super();
        name = NAME;
    }

    @Override
    protected List<Object> mutateObject(List<Object> value) throws CouldNotApplyOperatorException {
        throwIfEmpty(value);
        return new ArrayList<>();
    }
}
