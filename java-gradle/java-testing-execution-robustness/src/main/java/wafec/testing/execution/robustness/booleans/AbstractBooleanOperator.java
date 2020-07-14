package wafec.testing.execution.robustness.booleans;

import wafec.testing.execution.robustness.DataCorruption;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractBooleanOperator extends JsonBaseOperator<Boolean> {
    public AbstractBooleanOperator() {
        super();
        category = DataCorruption.BOOLEAN;
        dataType = Boolean.class.getSimpleName();
    }
}
