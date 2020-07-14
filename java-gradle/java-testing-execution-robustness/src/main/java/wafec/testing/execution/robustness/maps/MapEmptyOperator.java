package wafec.testing.execution.robustness.maps;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.HashMap;

public class MapEmptyOperator extends AbstractMapOperator {
    public static final String NAME = MapEmptyOperator.class.getSimpleName();

    public MapEmptyOperator() {
        super();
        name = NAME;
    }

    @Override
    protected HashMap<String, Object> mutateObject(HashMap<String, Object> value) throws CouldNotApplyOperatorException {
        return new HashMap<>();
    }
}
