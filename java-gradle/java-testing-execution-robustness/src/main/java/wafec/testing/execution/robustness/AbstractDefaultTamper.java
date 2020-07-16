package wafec.testing.execution.robustness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public abstract class AbstractDefaultTamper implements DataTamper {
    @Autowired
    protected InjectionFaultRepository injectionFaultRepository;
    @Autowired
    protected InjectionTargetRepository injectionTargetRepository;

    protected final int amountOfFaultPerTest = getAmountOfFaultPerTest();
    private Random rand = new Random();

    private Logger logger = LoggerFactory.getLogger(AbstractDefaultTamper.class);

    public AbstractDefaultTamper() {

    }

    protected abstract int getAmountOfFaultPerTest();

    @Override
    public Object adulterate(Object data, String sourceKey, String context,
                             RobustnessTestExecution robustnessTestExecution) {
        if (robustnessTestExecution == null)
            throw new IllegalArgumentException("Context not set");
        var robustnessTest = robustnessTestExecution.getRobustnessTest();
        InjectionFault injectionFault = new InjectionFault();
        InjectionTarget injectionTarget;
        var injectionTargetResult = injectionTargetRepository.findBySourceKeyAndContextAndRobustnessTest(sourceKey, context, robustnessTest);
        if (injectionTargetResult.size() > 0) {
            injectionTarget = injectionTargetResult.get(0);
            if (injectionTarget.isDiscard()) {
                return data;
            }
        }
        else {
            injectionTarget = new InjectionTarget();
            injectionTarget.setRobustnessTest(robustnessTest);
            injectionTarget.setSourceKey(sourceKey);
            injectionTarget.setContext(context);
        }
        injectionTarget.setOccurrences(injectionTarget.getOccurrences() + 1);
        injectionTargetRepository.save(injectionTarget);
        injectionFault.setInjectionTarget(injectionTarget);
        injectionFault.setRobustnessTestExecution(robustnessTestExecution);
        injectionFault.setUsed(false);
        injectionFaultRepository.save(injectionFault);

        if (!robustnessTestExecution.isStopFaultPropagation() && !robustnessTestExecution.isScan()) {
            long n = injectionFaultRepository.countByRobustnessTestExecutionAndUsedAndNotDiscard(
                    robustnessTestExecution);
            if (n < amountOfFaultPerTest) {
                var listOfUsed = injectionFaultRepository
                        .findByRobustnessTestAndUsedAndNotDiscardDistinctSourceKeyAndContext(robustnessTest);
                if (listOfUsed.stream().noneMatch(k -> k.equals(injectionTarget.getInjectionTargetUniqueKey()))) {
                    var listOfAll = injectionFaultRepository.findByRobustnessTestAndNotDiscardDistinctSourceKeyAndContext(robustnessTest);
                    var listOfAllOfExecution = injectionFaultRepository
                            .findByRobustnessTestExecutionAndNotDiscardDistinctSourceKeyAndContext(robustnessTestExecution);
                    var available = listOfAll.size() - listOfAllOfExecution.size();
                    if (available <= (amountOfFaultPerTest - n)) {
                        injectionFault.setUsed(true);
                        data = willAdulterate(data, sourceKey, context, robustnessTestExecution, injectionFault);
                    } else {
                        double randValue;
                        if ((randValue = rand.nextDouble()) > 0.5) {
                            logger.info(String.format("<Data(source=%s, context=%s)> picked up with rand of '%s'",
                                    sourceKey, context, randValue));
                            injectionFault.setUsed(true);
                            data = willAdulterate(data, sourceKey, context, robustnessTestExecution, injectionFault);
                        }
                    }
                }
            }

            injectionFaultRepository.save(injectionFault);
        }

        return data;
    }

    protected abstract Object willAdulterate(Object data, String sourceKey, String context, RobustnessTestExecution robustnessTestExecution,
                                             InjectionFault injectionFault);
}
