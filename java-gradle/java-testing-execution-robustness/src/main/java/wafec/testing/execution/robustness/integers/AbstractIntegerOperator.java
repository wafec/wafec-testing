package wafec.testing.execution.robustness.integers;

import wafec.testing.execution.robustness.DataTamper;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractIntegerOperator extends JsonBaseOperator<Integer> {
    public AbstractIntegerOperator() {
        super();
        category = DataTamper.NUMBER;
        dataType = Integer.class.getSimpleName();
    }
}
