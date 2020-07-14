package wafec.testing.execution.robustness.maps;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.HashMap;

public class MapNullOperator extends AbstractMapOperator {
    public static final String NAME = MapNullOperator.class.getSimpleName();

    public MapNullOperator() {
        super();
        name = NAME;
    }

    @Override
    protected HashMap<String, Object> mutateObject(HashMap<String, Object> value) throws CouldNotApplyOperatorException {
        return null;
    }
}
