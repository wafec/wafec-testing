package wafec.testing.execution.robustness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wafec.testing.execution.EnvironmentController;
import wafec.testing.execution.EnvironmentException;
import wafec.testing.execution.robustness.operators.kanso.ComputerShutdownOperator;
import wafec.testing.execution.robustness.operators.kanso.KansoOperator;
import wafec.testing.execution.robustness.operators.kanso.ServiceShutdownOperator;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class KansoInjectionManager implements InjectionManager {
    @Autowired
    private InjectionFaultRepository injectionFaultRepository;
    @Autowired
    private InjectionTargetOperatorRepository injectionTargetOperatorRepository;
    @Autowired
    private InjectionTargetRepository injectionTargetRepository;
    @Autowired
    private InjectionTargetManagedRepository injectionTargetManagedRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private EnvironmentController _environmentController;
    private Logger logger = LoggerFactory.getLogger(KansoInjectionManager.class);

    public KansoInjectionManager(EnvironmentController environmentController) {
        if (environmentController == null)
            throw new NullPointerException("Environment Controller cannot be null");
        _environmentController = environmentController;
    }

    @Override
    public void handleInjectionManaged(Object data, InjectionTarget injectionTarget) {
        var robustnessTest = injectionTarget.getRobustnessTest();
        List<InjectionTargetManaged> managedList = injectionTargetManagedRepository.findByRobustnessTestAndSourceKey(robustnessTest, injectionTarget.getSourceKey());
        try {
            List<KansoOperator> operators = getKansoOperators();
            for (var operator : operators) {
                var managedOpt = managedList.stream().filter(m -> m.getInjectorName().equals(operator.getInjectionKey()))
                        .findFirst();
                if (managedOpt.isEmpty()) {
                    InjectionTargetManaged managed = new InjectionTargetManaged();
                    managed.setInjectionCount(0);
                    managed.setInjectionTarget(injectionTarget);
                    managed.setInjectorName(operator.getInjectionKey());
                    injectionTargetManagedRepository.save(managed);
                }
            }
        } catch (EnvironmentException exc) {
            logger.error(exc.getMessage(), exc);
        }
    }

    private void incrementInjectionManagedCount(InjectionFault injectionFault, KansoOperator operator) {
        var injectionTarget = injectionFault.getInjectionTarget();
        var robustnessTest = injectionTarget.getRobustnessTest();
        var sourceKey = injectionTarget.getSourceKey();
        var managedResult = injectionTargetManagedRepository.findByRobustnessTestAndSourceKeyAndInjectorName(robustnessTest,
                sourceKey, operator.getInjectionKey());
        for (var managed : managedResult) {
            managed.setInjectionCount(managed.getInjectionCount() + 1);
        }
        injectionTargetManagedRepository.saveAll(managedResult);
    }

    @Override
    public Object inject(Object data, InjectionFault injectionFault) {
        var injectionTarget = injectionFault.getInjectionTarget();
        var robustnessTest = injectionTarget.getRobustnessTest();

        var operators = injectionTargetOperatorRepository.findByRobustnessTestAndSourceKey(robustnessTest, injectionTarget.getSourceKey());
        try {
            if (operators.size() == 0) {
                createInjectionTargetOperatorList(injectionTarget);
            }

            var operator = selectOperatorFromList(injectionTarget, injectionFault);
            try {
                if (operator != null) {
                    operator.injectFault(injectionFault);
                    incrementInjectionManagedCount(injectionFault, operator);
                } else {
                    injectionFault.setUsed(false);
                    injectionFault.setPersist(true);
                }
            } catch (CouldNotApplyOperatorException exc) {
                injectionFault.setUsed(true);
                injectionFault.setPersist(true);
            }
        } catch (EnvironmentException exc) {
            injectionFault.setUsed(false);
            injectionFault.setPersist(true);
            logger.error(exc.getMessage(), exc);
        }

        injectionFaultRepository.save(injectionFault);

        return data;
    }

    private void createInjectionTargetOperatorList(InjectionTarget injectionTarget) throws EnvironmentException {
        var operators = getKansoOperators();
        for (var operator : operators) {
            InjectionTargetOperator injectionTargetOperator = new InjectionTargetOperator();
            injectionTargetOperator.setInjectionTarget(injectionTarget);
            injectionTargetOperator.setInjectionKey(operator.getInjectionKey());
            injectionTargetOperator.setUsed(false);
            injectionTargetOperator.setIgnored(false);

            injectionTargetOperatorRepository.save(injectionTargetOperator);
        }
    }

    private List<KansoOperator> getKansoOperators() throws EnvironmentException {
        List<KansoOperator> kansoOperators = new ArrayList<>();

        for (var hostName : _environmentController.getNodeNames()) {
            kansoOperators.add(applicationContext.getBean(ComputerShutdownOperator.class, _environmentController, hostName));
            for (var serviceName : _environmentController.getServiceProcessNames(hostName)) {
                kansoOperators.add(applicationContext.getBean(ServiceShutdownOperator.class, _environmentController, hostName, serviceName));
            }
        }

        return kansoOperators;
    }

    private KansoOperator selectOperatorFromList(InjectionTarget injectionTarget, InjectionFault injectionFault) throws EnvironmentException {
        var robustnessTest = injectionTarget.getRobustnessTest();
        List<InjectionTargetOperator> nonUsedOperators = injectionTargetOperatorRepository.findByRobustnessTestAndSourceKeyAndNotUsed(robustnessTest,
                injectionTarget.getSourceKey());
        if (nonUsedOperators.size() == 0)
            return null;
        var operators = getKansoOperators();
        for (var operator : operators) {
            var first = nonUsedOperators.stream().filter(o -> o.getInjectionKey().equals(operator.getInjectionKey())).findFirst();
            if (first.isPresent()) {
                var targetOperator = first.get();
                targetOperator.setUsed(true);
                injectionTargetOperatorRepository.save(targetOperator);
                injectionFault.setInjectionTargetOperator(targetOperator);
                injectionFault.setUsed(true);
                return operator;
            }
        }
        return null;
    }
}
