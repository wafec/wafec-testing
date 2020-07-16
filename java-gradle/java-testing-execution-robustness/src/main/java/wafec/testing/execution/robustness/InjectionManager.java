package wafec.testing.execution.robustness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.core.JsonSerializationUtils;
import wafec.testing.execution.robustness.booleans.BooleanOperator;
import wafec.testing.execution.robustness.booleans.BooleanOperatorUtils;
import wafec.testing.execution.robustness.integers.IntegerOperator;
import wafec.testing.execution.robustness.integers.IntegerOperatorUtils;
import wafec.testing.execution.robustness.strings.StringOperator;
import wafec.testing.execution.robustness.strings.StringOperatorUtils;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
public class InjectionManager {
    @Autowired
    private InjectionTargetOperatorRepository injectionTargetOperatorRepository;
    @Autowired
    private InjectionFaultRepository injectionFaultRepository;
    @Autowired
    private RobustnessTestExecutionRepository robustnessTestExecutionRepository;
    @Autowired
    private InjectionTargetRepository injectionTargetRepository;

    private double usagePercentage = 1.0;
    private InjectionTargetOperator currentInjectionTargetOperator;
    private ReentrantLock objectLock = new ReentrantLock();

    Logger logger = LoggerFactory.getLogger(InjectionManager.class);

    public void setUsagePercentage(double usagePercentage) {
        if (usagePercentage > 0.0 && usagePercentage <= 1.0) {
            this.usagePercentage = usagePercentage;
        } else {
            throw new IllegalArgumentException("Must accept between 0.1 and 1.0");
        }
    }

    public Object inject(Object data, InjectionFault injectionFault) {
        objectLock.lock();
        var injectionTarget = injectionFault.getInjectionTarget();
        try {
            if (data != null) {
                Object dataResult = null;
                if (BooleanOperatorUtils.isApplicable(data)) {
                    dataResult = injectBoolean(data, injectionFault);
                } else if (StringOperatorUtils.isApplicable(data)) {
                    dataResult = injectString(data, injectionFault);
                } else if (IntegerOperatorUtils.isApplicable(data)) {
                    dataResult = injectInteger(data, injectionFault);
                } else {
                    injectionFault.setUsed(false);
                    injectionTarget.setDiscard(true);
                    injectionTarget.setDescription(String.format("Could not handle '%s' type", data.getClass().getSimpleName()));
                    injectionTargetRepository.save(injectionTarget);
                    injectionFaultRepository.save(injectionFault);
                    logger.info(String.format("Such operator not available for '%s'", injectionFault.getInjectionTarget().getQualifiedDescription()));
                    return data;
                }
                currentInjectionTargetOperator.setInjectionValue(JsonSerializationUtils.trySerialize(dataResult, "Could not serialize"));
                injectionTargetOperatorRepository.save(currentInjectionTargetOperator);
                return dataResult;
            } else {
                return null;
            }
        } catch (CouldNotApplyOperatorException exc) {
            logger.warn(exc.getMessage(), exc);
            handleError(exc);
            return data;
        } catch (UnexpectedInjectorException exc) {
            logger.error(exc.getMessage(), exc);
            handleError(exc);
            return data;
        } finally {
            objectLock.unlock();
        }
    }

    private Object injectBoolean(Object data, InjectionFault injectionFault) throws
            CouldNotApplyOperatorException, UnexpectedInjectorException {
        var operators = BooleanOperatorUtils
                .getOperators();
        GenericTypeOperator operator = chooseOperator(data, injectionFault, operators.stream().map(o -> (GenericTypeOperator)o).collect(Collectors.toList()));
        return ((BooleanOperator) operator).mutateBoolean((Boolean) data);
    }

    private Object injectString(Object data, InjectionFault injectionFault) throws
            CouldNotApplyOperatorException, UnexpectedInjectorException {
        var operators = StringOperatorUtils.getOperators();
        GenericTypeOperator operator = chooseOperator(data, injectionFault, operators.stream().map(o -> (GenericTypeOperator)o).collect(Collectors.toList()));
        return ((StringOperator) operator).mutateString((String) data);
    }

    private Object injectInteger(Object data, InjectionFault injectionFault) throws
            CouldNotApplyOperatorException, UnexpectedInjectorException {
        var operators = IntegerOperatorUtils.getOperators();
        GenericTypeOperator operator = chooseOperator(data, injectionFault, operators.stream().map(o -> (GenericTypeOperator)o).collect(Collectors.toList()));
        return ((IntegerOperator)operator).mutateInteger((Integer)data);
    }

    private GenericTypeOperator chooseOperator(Object data, InjectionFault injectionFault, List<GenericTypeOperator> operators) throws
            UnexpectedInjectorException {
        var injectionTarget = injectionFault.getInjectionTarget();
        var robustnessTestExecution = injectionFault.getRobustnessTestExecution();
        var injectionTargetOperatorList =
                injectionTargetOperatorRepository.findByInjectionTarget(injectionTarget);
        if (injectionTargetOperatorList.size() == 0) {
            for (var operator : operators) {
                InjectionTargetOperator injectionTargetOperator = new InjectionTargetOperator();
                injectionTargetOperator.setInjectionKey(operator.getInjectionKey());
                injectionTargetOperator.setInjectionTarget(injectionTarget);
                injectionTargetOperator.setUsed(false);
                injectionTargetOperator.setIgnored(false);
                injectionTargetOperatorRepository.save(injectionTargetOperator);
                injectionTargetOperatorList.add(injectionTargetOperator);
            }
        }
        var operatorOpt = injectionTargetOperatorList.stream().filter(o -> !o.isUsed()).findFirst();
        if (operatorOpt.isEmpty())
            throw new UnexpectedInjectorException(String.format("NotUsed operator not found in list [%s]",
                    injectionTargetOperatorList.stream().map(InjectionTargetOperator::getInjectionKey).collect(Collectors.joining(", "))));
        var operator = operatorOpt.get();
        operator.setFieldValue(JsonSerializationUtils.trySerialize(data, "Could not serialize"));
        int capacity = Math.max(1, (int)(injectionTargetOperatorList.size() * usagePercentage));
        operator.setUsed(true);
        injectionTargetOperatorRepository.save(operator);
        long amountOfNotUsed = injectionTargetOperatorList.stream().filter(o -> o.isUsed()).count();
        if (amountOfNotUsed < capacity) {
            injectionFault.setUsed(false);
            injectionFaultRepository.save(injectionFault);
            robustnessTestExecution.setStopFaultPropagation(true);
            robustnessTestExecutionRepository.save(robustnessTestExecution);
        }
        currentInjectionTargetOperator = operator;
        logger.info(String.format("Will inject '%s' on <Data(value=%s, type=%s, source=%s, context=%s)>", operator.getInjectionKey(),
                data, data.getClass().getName(), injectionTarget.getSourceKey(), injectionTarget.getContext()));
        var objectOperatorOpt = operators.stream().filter(o -> o.getInjectionKey().equals(operator.getInjectionKey()))
                .findFirst();
        if (objectOperatorOpt.isEmpty())
            throw new UnexpectedInjectorException(String.format("Something went wrong on filtering by '%s' in list [%s]",
                    operator.getInjectionKey(), operators.stream().map(GenericTypeOperator::getInjectionKey).collect(Collectors.joining(", "))));
        return objectOperatorOpt.get();
    }

    private void handleError(Exception exc) {
        if (currentInjectionTargetOperator != null) {
            currentInjectionTargetOperator.setIgnored(true);
            currentInjectionTargetOperator.setDescription(exc.getMessage());
            injectionTargetOperatorRepository.save(currentInjectionTargetOperator);
        }
    }
}
