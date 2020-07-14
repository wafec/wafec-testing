package wafec.testing.execution.robustness.maps;

import wafec.testing.execution.robustness.DataCorruption;
import wafec.testing.execution.robustness.JsonBaseOperator;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMapOperator extends JsonBaseOperator<HashMap<String, Object>> {
    public AbstractMapOperator() {
        super();
        category = DataCorruption.MAP;
        dataType = Map.class.getSimpleName();
    }
}
