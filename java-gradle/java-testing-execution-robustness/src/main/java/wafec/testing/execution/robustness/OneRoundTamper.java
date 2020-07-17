package wafec.testing.execution.robustness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OneRoundTamper extends AbstractDefaultTamper {
    @Autowired
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
        return injectionManager.inject(data, injectionFault);
    }
}