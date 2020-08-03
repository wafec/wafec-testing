package wafec.testing.execution.robustness.operators.jsonBased.maps;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.DataTamper;
import wafec.testing.execution.robustness.JsonBaseOperator;

import java.util.Map;

public abstract class AbstractMapOperator extends JsonBaseOperator<Map<String, Object>> implements MapOperator {
    public AbstractMapOperator() {
        super();
        category = DataTamper.MAP;
        dataType = Map.class.getSimpleName();
    }

    protected void throwIfEmpty(Map<String, Object> data) throws CouldNotApplyOperatorException {
        if (data == null || data.size() == 0)
            throw new CouldNotApplyOperatorException("Map cannot be null or empty");
    }

    @Override
    public Map<String, Object> mutateMap(Map<String, Object> data) throws CouldNotApplyOperatorException {
        return mutateObject(data);
    }
}
