package wafec.testing.execution.robustness.strings;

import wafec.testing.execution.robustness.DataCorruption;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractStringOperator extends JsonBaseOperator<String> {
    public AbstractStringOperator() {
        super();
        category = DataCorruption.STRING;
        dataType = String.class.getSimpleName();
    }
}
