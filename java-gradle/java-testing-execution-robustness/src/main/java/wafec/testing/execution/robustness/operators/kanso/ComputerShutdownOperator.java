package wafec.testing.execution.robustness.operators.kanso;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wafec.testing.execution.EnvironmentController;
import wafec.testing.execution.EnvironmentException;
import wafec.testing.execution.robustness.GenericTypeOperator;
import wafec.testing.execution.robustness.InjectionFault;

@Component
@Scope("prototype")
public class ComputerShutdownOperator implements GenericTypeOperator, KansoOperator {
    private String targetHostName;
    private EnvironmentController environmentController;

    public ComputerShutdownOperator(EnvironmentController environmentController, String targetHostName) {
        this.environmentController = environmentController;
        this.targetHostName = targetHostName;
    }

    @Override
    public String getInjectionKey() {
        return String.format("computer.%s.shutdown", targetHostName);
    }

    @Override
    public void injectFault(InjectionFault injectionFault) throws EnvironmentException {
        environmentController.shutdownNode(targetHostName);
    }
}
