package wafec.testing.execution.robustness.operators.kanso;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wafec.testing.execution.EnvironmentController;
import wafec.testing.execution.EnvironmentException;
import wafec.testing.execution.robustness.GenericTypeOperator;
import wafec.testing.execution.robustness.InjectionFault;

@Component
@Scope("prototype")
public class ServiceShutdownOperator implements KansoOperator {
    private EnvironmentController environmentController;
    private String targetServiceName;
    private String targetNodeName;

    public ServiceShutdownOperator(EnvironmentController environmentController, String targetNodeName, String targetServiceName) {
        this.environmentController = environmentController;
        this.targetServiceName = targetServiceName;
        this.targetNodeName = targetNodeName;
    }

    @Override
    public String getInjectionKey() {
        return String.format("service.%s.shutdown", targetServiceName);
    }

    @Override
    public void injectFault(InjectionFault injectionFault) throws EnvironmentException {
        environmentController.stopServiceProcess(targetNodeName, targetServiceName);
    }
}