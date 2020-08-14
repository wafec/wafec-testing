package wafec.testing.execution.robustness;

import org.springframework.stereotype.Component;

@Component
public class KansoInjectionManager implements InjectionManager {
    @Override
    public void handleInjectionManaged(Object data, InjectionTarget injectionTarget) {

    }

    @Override
    public Object inject(Object data, InjectionFault injectionFault) {
        
        return data;
    }
}
