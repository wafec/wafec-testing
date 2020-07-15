package wafec.testing.execution.robustness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractDataTamper implements DataTamper {
    @Autowired
    private InjectionFaultRepository injectionFaultRepository;

    protected int amountOfFaultPerTest;

    public AbstractDataTamper() {
        amountOfFaultPerTest = 1;
    }

    @Override
    public Object adulterate(Object data, String sourceKey, String context,
                             RobustnessTestExecution robustnessTestExecution) {
        if (robustnessTestExecution == null)
            throw new IllegalArgumentException("Context not set");
        InjectionFault injectionFault = new InjectionFault();
        injectionFault.setContext(context);
        injectionFault.setSourceKey(sourceKey);
        injectionFault.setRobustnessTestExecution(robustnessTestExecution);
        injectionFault.setUsed(false);
        long n = injectionFaultRepository.countByRobustnessTestExecutionAndUsed(robustnessTestExecution);
        if (n < amountOfFaultPerTest) {
            var listOfUsed = injectionFaultRepository.findByRobustnessTestAndUsedDistinctSourceKeyAndContext(robustnessTestExecution.getRobustnessTest());
            if (listOfUsed.stream().noneMatch(k -> k.equals(String.format("%s %s", injectionFault.getSourceKey(), injectionFault.getContext())))) {
                injectionFault.setUsed(true);
            }
        }
        injectionFaultRepository.save(injectionFault);
        return data;
    }
}
