package wafec.testing.execution.robustness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.execution.TestExecutionInputRepository;
import wafec.testing.execution.TestInput;
import wafec.testing.execution.TestInputRepository;

import java.util.Date;
import java.util.Random;

@Component
public abstract class AbstractDefaultTamper implements DataTamper {
    @Autowired
    protected InjectionFaultRepository injectionFaultRepository;
    @Autowired
    protected InjectionTargetRepository injectionTargetRepository;
    @Autowired
    protected TestExecutionInputRepository testExecutionInputRepository;
    @Autowired
    protected TestInputRepository testInputRepository;

    protected final int amountOfFaultPerTest = getAmountOfFaultPerTest();
    private Random rand = new Random();
    protected double selectionThreshold = 0.5;

    private Logger logger = LoggerFactory.getLogger(AbstractDefaultTamper.class);

    private boolean debug = false;

    public AbstractDefaultTamper() {

    }

    protected abstract int getAmountOfFaultPerTest();

    protected boolean shouldIgnore(Object data, String sourceKey, String context,
                                   RobustnessTestExecution robustnessTestExecution) {
        var testExecution = robustnessTestExecution.getTestExecution();
        var importantList = testExecutionInputRepository.findByTestExecutionAndImportant(testExecution);
        if (importantList.size() > 0) {
            var inUseInputList = testInputRepository.findByTestExecutionAndStatusIsInUse(testExecution);
            if (inUseInputList.stream().noneMatch(TestInput::isImportant)) {
                if (debug)
                    logger.debug(String.format("Will ignore '%s.%s'. It is not important.", sourceKey, context));
                return true;
            }
        }
        return false;
    }

    @Override
    public Object adulterate(Object data, String sourceKey, String context,
                             RobustnessTestExecution robustnessTestExecution) {
        if (robustnessTestExecution == null)
            throw new IllegalArgumentException("Context not set");
        if (shouldIgnore(data, sourceKey, context, robustnessTestExecution))
            return data;
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
            injectionTarget.setFirstInitialized(true);
        }
        injectionTarget.setMonitorCount(injectionTarget.getMonitorCount() + 1);
        injectionTargetRepository.save(injectionTarget);

        if (injectionTarget.isFirstInitialized())
            handleInjectionTargetSaved(injectionTarget, data, sourceKey, context, robustnessTestExecution);

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
                        injectionFault.setUsedAt(new Date());
                        data = willAdulterate(data, sourceKey, context, robustnessTestExecution, injectionFault);
                    } else {
                        double randValue;
                        if ((randValue = rand.nextDouble()) > Math.max(0.0, Math.min(0.9, selectionThreshold))) {
                            logger.info(String.format("<Data(source=%s, context=%s)> picked up with rand of '%s'",
                                    sourceKey, context, randValue));
                            injectionFault.setUsed(true);
                            injectionFault.setUsedAt(new Date());
                            data = willAdulterate(data, sourceKey, context, robustnessTestExecution, injectionFault);
                        }
                    }
                }
            }

            if (!injectionFault.isPersist() && !injectionFault.isUsed())
                injectionFaultRepository.delete(injectionFault);

            injectionFaultRepository.save(injectionFault);
        }

        return data;
    }

    protected abstract Object willAdulterate(Object data, String sourceKey, String context, RobustnessTestExecution robustnessTestExecution,
                                             InjectionFault injectionFault);

    protected void handleInjectionTargetSaved(InjectionTarget injectionTarget, Object data, String sourceKey,
                                              String context, RobustnessTestExecution robustnessTestExecution) {

    }
}
