package wafec.testing.execution.robustness.strings;

import wafec.testing.execution.robustness.DataTamper;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractStringOperator extends JsonBaseOperator<String> {
    public AbstractStringOperator() {
        super();
        category = DataTamper.STRING;
        dataType = String.class.getSimpleName();
    }
}
