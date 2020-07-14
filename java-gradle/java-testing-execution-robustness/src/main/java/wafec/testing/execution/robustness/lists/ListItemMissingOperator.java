package wafec.testing.execution.robustness.lists;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.List;
import java.util.Random;

public class ListItemMissingOperator extends AbstractListOperator {
    public static final String NAME = ListItemMissingOperator.class.getSimpleName();

    public ListItemMissingOperator() {
        super();
        name = NAME;
    }

    @Override
    protected List<Object> mutateObject(List<Object> value) throws CouldNotApplyOperatorException {
        throwIfEmpty(value);
        value.remove(new Random().nextInt(value.size()));
        return value;
    }
}
