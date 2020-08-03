package wafec.testing.execution.robustness.operators.jsonBased.maps;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.Map;

public class MapNullOperator extends AbstractMapOperator {
    public static final String NAME = MapNullOperator.class.getSimpleName();

    public MapNullOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Map<String, Object> mutateObject(Map<String, Object> value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        return null;
    }
}
