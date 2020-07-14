package wafec.testing.execution.robustness.doubles;

import wafec.testing.execution.robustness.DataCorruption;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractDoubleOperator extends JsonBaseOperator<Double> {
    public AbstractDoubleOperator() {
        super();
        category = DataCorruption.NUMBER;
        dataType = Double.class.getSimpleName();
    }
}
