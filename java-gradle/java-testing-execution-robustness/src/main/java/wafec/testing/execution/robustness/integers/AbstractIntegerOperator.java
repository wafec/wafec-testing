package wafec.testing.execution.robustness.integers;

import wafec.testing.execution.robustness.DataCorruption;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractIntegerOperator extends JsonBaseOperator<Integer> {
    public AbstractIntegerOperator() {
        super();
        category = DataCorruption.NUMBER;
        dataType = Integer.class.getSimpleName();
    }
}
