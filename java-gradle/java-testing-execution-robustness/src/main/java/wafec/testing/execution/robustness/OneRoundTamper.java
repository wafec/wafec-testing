package wafec.testing.execution.robustness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OneRoundTamper extends AbstractDefaultTamper {
    @Autowired
    private JsonInjectionManager defaultInjectionManager;

    private InjectionManager injectionManager;

    public OneRoundTamper() {
        super();
    }

    @Override
    protected int getAmountOfFaultPerTest() {
        return 1;
    }

    @Override
    protected Object willAdulterate(Object data, String sourceKey, String context, RobustnessTestExecution robustnessTestExecution, InjectionFault injectionFault) {
        return getInjectionManager().inject(data, injectionFault);
    }

    @Override
    protected void handleInjectionTargetSaved(InjectionTarget injectionTarget, Object data, String sourceKey, String context, RobustnessTestExecution robustnessTestExecution) {
        getInjectionManager().handleInjectionManaged(data, injectionTarget);
    }

    public void setInjectionManager(InjectionManager injectionManager) {
        this.injectionManager = injectionManager;
    }

    private InjectionManager getInjectionManager() {
        if (injectionManager == null)
            injectionManager = defaultInjectionManager;
        return injectionManager;
    }
}
