package wafec.testing.execution.robustness.lists;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.DataCorruption;
import wafec.testing.execution.robustness.JsonBaseOperator;

import java.util.List;

public abstract class AbstractListOperator extends JsonBaseOperator<List<Object>> {
    public AbstractListOperator() {
        super();
        category = DataCorruption.LIST;
        dataType = List.class.getSimpleName();
    }
}
