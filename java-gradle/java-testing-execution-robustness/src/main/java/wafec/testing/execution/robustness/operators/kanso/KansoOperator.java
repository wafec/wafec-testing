package wafec.testing.execution.robustness.operators.kanso;

import wafec.testing.execution.EnvironmentException;
import wafec.testing.execution.robustness.CouldNotApplyOperatorException;
import wafec.testing.execution.robustness.GenericTypeOperator;
import wafec.testing.execution.robustness.InjectionFault;

public interface KansoOperator extends GenericTypeOperator {
    void injectFault(InjectionFault injectionFault) throws CouldNotApplyOperatorException;
}
