package wafec.testing.execution.robustness.lists;

import wafec.testing.execution.robustness.DataTamper;
import wafec.testing.execution.robustness.JsonBaseOperator;

import java.util.List;

public abstract class AbstractListOperator extends JsonBaseOperator<List<Object>> {
    public AbstractListOperator() {
        super();
        category = DataTamper.LIST;
        dataType = List.class.getSimpleName();
    }
}
