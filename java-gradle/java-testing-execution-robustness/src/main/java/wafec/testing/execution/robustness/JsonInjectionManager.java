package wafec.testing.execution.robustness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.core.JsonSerializationUtils;
import wafec.testing.execution.robustness.operators.json.booleans.BooleanOperator;
import wafec.testing.execution.robustness.operators.json.booleans.BooleanOperatorUtils;
import wafec.testing.execution.robustness.operators.json.doubles.DoubleOperator;
import wafec.testing.execution.robustness.operators.json.doubles.DoubleOperatorUtils;
import wafec.testing.execution.robustness.operators.json.integers.IntegerOperator;
import wafec.testing.execution.robustness.operators.json.integers.IntegerOperatorUtils;
import wafec.testing.execution.robustness.operators.json.lists.ListOperator;
import wafec.testing.execution.robustness.operators.json.lists.ListOperatorUtils;
import wafec.testing.execution.robustness.operators.json.maps.MapOperator;
import wafec.testing.execution.robustness.operators.json.maps.MapOperatorUtils;
import wafec.testing.execution.robustness.operators.json.strings.StringOperator;
import wafec.testing.execution.robustness.operators.json.strings.StringOperatorUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
public class JsonInjectionManager implements InjectionManager {
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

    Logger logger = LoggerFactory.getLogger(JsonInjectionManager.class);

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

    @Override
    public void handleInjectionManaged(Object data, InjectionTarget injectionTarget) {
        try {
            objectLock.lock();

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

            if (operators != null) {
                for (var operator : operators) {
                    GenericTypeOperator genericOperator = (GenericTypeOperator) operator;
                    var managedOpt = managedList.stream().filter(m -> m.getInjectorName().equals(genericOperator.getInjectionKey()))
                            .findFirst();
                    if (managedOpt.isEmpty()) {
                        InjectionTargetManaged managed = new InjectionTargetManaged();
                        managed.setInjectionCount(0);
                        managed.setInjectionTarget(injectionTarget);
                        managed.setInjectorName(genericOperator.getInjectionKey());
                        managedList.add(managed);
                        injectionTargetManagedRepository.save(managed);
                    }
                }
            }
        } finally {
            objectLock.unlock();
        }
    }

    private void incrementInjectionManagedCount(String injectionKey, InjectionTarget injectionTarget) {
        var managedList = injectionTargetManagedRepository.findByInjectionTargetAndInjectorName(injectionTarget, injectionKey);
        for (var managed : managedList) {
            managed.setInjectionCount(managed.getInjectionCount() + 1);
        }
        injectionTargetManagedRepository.saveAll(managedList);
    }

    @Override
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
                    injectionFault.setPersist(true);
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
                
                injectionFault.setUsed(true);
                injectionFault.setInjectionTargetOperator(currentInjectionTargetOperator);
                injectionFaultRepository.save(injectionFault);

                incrementInjectionManagedCount(currentInjectionTargetOperator.getInjectionKey(), injectionTarget);

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
            throw new UnexpectedInjectorException(String.format("Non-used operator not found in list [%s]",
                    injectionTargetOperatorList.stream().map(InjectionTargetOperator::getInjectionKey).collect(Collectors.joining(", "))));
        var operator = operatorOpt.get();
        operator.setFieldValue(JsonSerializationUtils.trySerialize(data, "Could not serialize"));
        operator.setUsed(true);
        injectionTargetOperatorRepository.save(operator);
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
