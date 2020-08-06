package wafec.testing.execution.robustness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.core.JsonSerializationUtils;
import wafec.testing.execution.robustness.operators.jsonBased.booleans.BooleanOperator;
import wafec.testing.execution.robustness.operators.jsonBased.booleans.BooleanOperatorUtils;
import wafec.testing.execution.robustness.operators.jsonBased.doubles.DoubleOperator;
import wafec.testing.execution.robustness.operators.jsonBased.doubles.DoubleOperatorUtils;
import wafec.testing.execution.robustness.operators.jsonBased.integers.IntegerOperator;
import wafec.testing.execution.robustness.operators.jsonBased.integers.IntegerOperatorUtils;
import wafec.testing.execution.robustness.operators.jsonBased.lists.ListOperator;
import wafec.testing.execution.robustness.operators.jsonBased.lists.ListOperatorUtils;
import wafec.testing.execution.robustness.operators.jsonBased.maps.MapOperator;
import wafec.testing.execution.robustness.operators.jsonBased.maps.MapOperatorUtils;
import wafec.testing.execution.robustness.operators.jsonBased.strings.StringOperator;
import wafec.testing.execution.robustness.operators.jsonBased.strings.StringOperatorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private InjectionTargetManagedRepository injectionTargetManagedRepository;

    private double usagePercentage = 1.0;
    private InjectionTargetOperator currentInjectionTargetOperator;
    private ReentrantLock objectLock = new ReentrantLock();

    public static final String INTEGER = "integer";
    public static final String DOUBLE = "double";
    public static final String BOOLEAN = DataTamper.BOOLEAN;
    public static final String LIST = DataTamper.LIST;
    public static final String OBJECT = DataTamper.OBJECT;
    public static final String MAP = DataTamper.MAP;
    public static final String NULL = "null";
    public static final String STRING = DataTamper.STRING;

    Logger logger = LoggerFactory.getLogger(InjectionManager.class);

    public void setUsagePercentage(double usagePercentage) {
        if (usagePercentage > 0.0 && usagePercentage <= 1.0) {
            this.usagePercentage = usagePercentage;
        } else {
            throw new IllegalArgumentException("Must accept between 0.1 and 1.0");
        }
    }

    private String getMutationDataType(Object data) {
        if (data == null)
            return NULL;

        if (BooleanOperatorUtils.isApplicable(data)) {
            return BOOLEAN;
        } else if (StringOperatorUtils.isApplicable(data)) {
            return STRING;
        } else if (IntegerOperatorUtils.isApplicable(data)) {
            return INTEGER;
        } else if (DoubleOperatorUtils.isApplicable(data)) {
            return DOUBLE;
        } else if (ListOperatorUtils.isApplicable(data)) {
            return LIST;
        } else if (MapOperatorUtils.isApplicable(data)) {
            return MAP;
        } else {
            return OBJECT;
        }
    }

    public void updateByInjectionTarget(Object data, InjectionTarget injectionTarget) {
        if (injectionTarget == null)
            throw new IllegalArgumentException("injectionTarget must be instantiated");
        if (injectionTarget.isDiscard())
            return;
        injectionTarget.setDataType(getMutationDataType(data));
        List operators = null;
        List<InjectionTargetManaged> managedList = injectionTargetManagedRepository.findByInjectionTarget(injectionTarget);
        switch (injectionTarget.getDataType()) {
            case BOOLEAN:
                operators = BooleanOperatorUtils.getOperators();
                break;
            case LIST:
                operators = ListOperatorUtils.getOperators();
                break;
            case INTEGER:
                operators = IntegerOperatorUtils.getOperators();
                break;
            case DOUBLE:
                operators = DoubleOperatorUtils.getOperators();
                break;
            case STRING:
                operators = StringOperatorUtils.getOperators();
                break;
            case MAP:
                operators = MapOperatorUtils.getOperators();
                break;
        }

        managedList.forEach(m -> m.setInUse(false));
        if (operators != null) {
            for (var operator : operators) {
                GenericTypeOperator genericOperator = (GenericTypeOperator) operator;
                if (managedList.stream().anyMatch(m -> m.getInjectorName().equals(genericOperator.getInjectionKey()))) {
                    var managed = managedList.stream().filter(m -> m.getInjectorName().equals(genericOperator.getInjectionKey())).findFirst()
                            .orElseThrow();
                    managed.setInUse(true);
                } else {
                    InjectionTargetManaged managed = new InjectionTargetManaged();
                    managed.setInUse(true);
                    managed.setInjectionTarget(injectionTarget);
                    managed.setInjectorName(genericOperator.getInjectionKey());
                    managedList.add(managed);
                }
            }
        }
        injectionTargetManagedRepository.saveAll(managedList);
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
                } else if (ListOperatorUtils.isApplicable(data)) {
                    dataResult = injectList(data, injectionFault);
                } else if (DoubleOperatorUtils.isApplicable(data)) {
                    dataResult = injectDouble(data, injectionFault);
                } else if (MapOperatorUtils.isApplicable(data)) {
                    dataResult = injectMap(data, injectionFault);
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
        GenericTypeOperator operator = chooseOperator(data, injectionFault, operators);
        return ((BooleanOperator) operator).mutateBoolean((Boolean) data);
    }

    private Object injectString(Object data, InjectionFault injectionFault) throws
            CouldNotApplyOperatorException, UnexpectedInjectorException {
        var operators = StringOperatorUtils.getOperators();
        GenericTypeOperator operator = chooseOperator(data, injectionFault, operators);
        return ((StringOperator) operator).mutateString((String) data);
    }

    private Object injectInteger(Object data, InjectionFault injectionFault) throws
            CouldNotApplyOperatorException, UnexpectedInjectorException {
        var operators = IntegerOperatorUtils.getOperators();
        GenericTypeOperator operator = chooseOperator(data, injectionFault, operators);
        return ((IntegerOperator)operator).mutateInteger((Integer)data);
    }

    private Object injectList(Object data, InjectionFault injectionFault) throws
            CouldNotApplyOperatorException, UnexpectedInjectorException {
        var operators = ListOperatorUtils.getOperators();
        GenericTypeOperator operator = chooseOperator(data, injectionFault, operators);
        return ((ListOperator) operator).mutateList((List) data);
    }

    private Object injectDouble(Object data, InjectionFault injectionFault) throws
            CouldNotApplyOperatorException, UnexpectedInjectorException {
        var operators = DoubleOperatorUtils.getOperators();
        GenericTypeOperator operator = chooseOperator(data, injectionFault, operators);
        return ((DoubleOperator) operator).mutateDouble((Double) data);
    }

    private Object injectMap(Object data, InjectionFault injectionFault) throws
            CouldNotApplyOperatorException, UnexpectedInjectorException {
        var operators = MapOperatorUtils.getOperators();
        GenericTypeOperator operator = chooseOperator(data, injectionFault, operators);
        return ((MapOperator) operator).mutateMap((Map<String, Object>) data);
    }

    private <T extends GenericTypeOperator> GenericTypeOperator chooseOperator(Object data, InjectionFault injectionFault,
                                                                               List<T> operators) throws
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
