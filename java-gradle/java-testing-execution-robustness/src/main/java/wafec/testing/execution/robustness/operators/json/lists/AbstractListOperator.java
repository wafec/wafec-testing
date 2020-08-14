package wafec.testing.execution.robustness.operators.json.lists;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.DataTamper;
import wafec.testing.execution.robustness.JsonBaseOperator;

import java.util.List;

public abstract class AbstractListOperator extends JsonBaseOperator<List<Object>> implements ListOperator {
    public AbstractListOperator() {
        super();
        category = DataTamper.LIST;
        dataType = List.class.getSimpleName();
    }

    @Override
    public List<Object> mutateList(List<Object> data) throws CouldNotApplyOperatorException {
        return mutateObject(data);
    }
}
