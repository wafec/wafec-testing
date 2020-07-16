package wafec.testing.execution.robustness.strings;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.DataTamper;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractStringOperator extends JsonBaseOperator<String> implements StringOperator {
    public AbstractStringOperator() {
        super();
        category = DataTamper.STRING;
        dataType = String.class.getSimpleName();
    }

    @Override
    public String mutateString(String data) throws CouldNotApplyOperatorException {
        return mutateObject(data);
    }
}
