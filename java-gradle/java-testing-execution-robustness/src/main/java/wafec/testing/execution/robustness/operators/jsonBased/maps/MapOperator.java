package wafec.testing.execution.robustness.operators.jsonBased.maps;

import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.GenericTypeOperator;

import java.util.Map;

public interface MapOperator extends GenericTypeOperator {
    Map<String, Object> mutateMap(Map<String, Object> data) throws CouldNotApplyOperatorException;
}
