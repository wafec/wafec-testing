package wafec.testing.execution.robustness.operators.jsonBased.maps;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MapKeyMissingOperator extends AbstractMapOperator {
    public static final String NAME = MapKeyMissingOperator.class.getSimpleName();

    public MapKeyMissingOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Map<String, Object> mutateObject(Map<String, Object> value) throws CouldNotApplyOperatorException {
        throwIfEmpty(new ArrayList<String>(value.keySet()));
        String missingKey = new ArrayList<>(value.keySet()).get(new Random().nextInt(value.keySet().size()));
        value.remove(missingKey);
        return value;
    }
}
