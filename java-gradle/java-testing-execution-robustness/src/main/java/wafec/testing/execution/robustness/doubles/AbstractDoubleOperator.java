package wafec.testing.execution.robustness.doubles;

import wafec.testing.execution.robustness.DataTamper;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractDoubleOperator extends JsonBaseOperator<Double> {
    public AbstractDoubleOperator() {
        super();
        category = DataTamper.NUMBER;
        dataType = Double.class.getSimpleName();
    }
}
