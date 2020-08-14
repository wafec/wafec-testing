package wafec.testing.execution.robustness.operators.json.maps;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.HashMap;
import java.util.Map;

public class MapEmptyOperator extends AbstractMapOperator {
    public static final String NAME = MapEmptyOperator.class.getSimpleName();

    public MapEmptyOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Map<String, Object> mutateObject(Map<String, Object> value) throws CouldNotApplyOperatorException {
        throwIfEmpty(value);
        return new HashMap<>();
    }
}
