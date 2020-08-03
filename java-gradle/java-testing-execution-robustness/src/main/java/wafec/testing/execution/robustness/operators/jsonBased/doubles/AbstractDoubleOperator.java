package wafec.testing.execution.robustness.operators.jsonBased.doubles;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.DataTamper;
import wafec.testing.execution.robustness.JsonBaseOperator;

public abstract class AbstractDoubleOperator extends JsonBaseOperator<Double> implements DoubleOperator {
    public AbstractDoubleOperator() {
        super();
        category = DataTamper.NUMBER;
        dataType = Double.class.getSimpleName();
    }

    @Override
    public Double mutateDouble(Double data) throws CouldNotApplyOperatorException {
        return mutateObject(data);
    }
}
