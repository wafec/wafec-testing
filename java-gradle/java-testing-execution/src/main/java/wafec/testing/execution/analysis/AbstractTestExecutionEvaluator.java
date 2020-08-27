package wafec.testing.execution.analysis;

import com.google.common.collect.Iterables;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import wafec.testing.execution.TestCaseManagedState;
import wafec.testing.execution.TestCaseManagedStateRepository;
import wafec.testing.execution.TestExecution;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTestExecutionEvaluator {
    @Autowired
    protected EvaluationTestExecutionRepository evaluationTestExecutionRepository;
    @Autowired
    protected EvaluationTestExecutionTraceRepository evaluationTestExecutionTraceRepository;
    @Autowired
    protected EvaluationTestExecutionTraceEntryRepository evaluationTestExecutionTraceEntryRepository;
    @Autowired
    protected TestCaseManagedStateRepository testCaseManagedStateRepository;
    @Autowired
    protected EvaluationTestExecutionSuiteRepository evaluationTestExecutionSuiteRepository;
    @Autowired
    protected EvaluationTestExecutionSuiteResultRepository evaluationTestExecutionSuiteResultRepository;

    public EvaluationTestExecution analyze(TestExecution testExecution) {
        final EvaluationTestExecution evaluationTestExecution = new EvaluationTestExecution();
        evaluationTestExecution.setGeneratedAt(new Date());
        evaluationTestExecution.setEvaluationTestExecutionStatus(null);
        evaluationTestExecution.setTestExecution(testExecution);
        evaluationTestExecutionRepository.save(evaluationTestExecution);
        var traceList = findTraces(evaluationTestExecution);
        traceList.forEach(trace -> {
            trace.setEvaluationTestExecution(evaluationTestExecution);
        });
        evaluationTestExecutionTraceRepository.saveAll(traceList);
        var withTextErrorPresent = traceList.stream().filter(t -> t.getTraceType().equals(EvaluationTestExecutionTraceTypes.FAILURE))
                .anyMatch(t -> {
                    return evaluationTestExecutionTraceEntryRepository.findByEvaluationTestExecutionTrace(t).size() > 0;
                });
        evaluationTestExecution.setWithTextErrorPresent(withTextErrorPresent);
        evaluationTestExecution.setEvaluationTestExecutionStatus(analyzeAndGetStatus(evaluationTestExecution));
        evaluationTestExecution.setPassSucceed(analyzeAndGetPassSucceed(evaluationTestExecution));
        evaluationTestExecutionRepository.save(evaluationTestExecution);
        return evaluationTestExecution;
    }

    protected abstract boolean analyzeAndGetPassSucceed(EvaluationTestExecution evaluationTestExecution);
    protected abstract List<EvaluationTestExecutionTrace> findTraces(EvaluationTestExecution evaluationTestExecution);

    protected EvaluationTestExecutionStatus analyzeAndGetStatus(EvaluationTestExecution evaluationTestExecution) {
        var evaluationTestExecutionTraceEntryList = evaluationTestExecutionTraceEntryRepository.findByEvaluationTestExecution(evaluationTestExecution);
        var testFunctionalList = evaluationTestExecutionTraceEntryList
                .stream()
                .filter(e -> e.getEvaluationTestExecutionTrace().getTraceType().equals(EvaluationTestExecutionTraceTypes.FUNCTIONAL) &&
                        e.getEvaluationTestExecutionTrace().getTestExecutionSourceType().equals(TestExecutionSourceTypes.TEST))
                .collect(Collectors.toList());
        var refFunctionalList = evaluationTestExecutionTraceEntryList
                .stream()
                .filter(e -> e.getEvaluationTestExecutionTrace().getTraceType().equals(EvaluationTestExecutionTraceTypes.FUNCTIONAL) &&
                        e.getEvaluationTestExecutionTrace().getTestExecutionSourceType().equals(TestExecutionSourceTypes.REFERENCE))
                .collect(Collectors.toList());
        testFunctionalList.sort(Comparator.comparing(e -> e.getTestExecutionObservedOutput().getTestOutput().getCreatedAt()));
        refFunctionalList.sort(Comparator.comparing(e -> e.getTestExecutionObservedOutput().getTestOutput().getCreatedAt()));
        if (testFunctionalList.size() > 0 && refFunctionalList.size() > 0) {
            if (Iterables.getLast(testFunctionalList).getDiscriminator().equals(Iterables.getLast(refFunctionalList).getDiscriminator())) {
                var descriptionCorrect = String.format("(%s) = (%s)",
                        testFunctionalList.stream().map(EvaluationTestExecutionTraceEntry::getDiscriminator).collect(Collectors.joining(" ")),
                        refFunctionalList.stream().map(EvaluationTestExecutionTraceEntry::getDiscriminator).collect(Collectors.joining(" "))
                );
                if (ListUtils.isEqualList(
                        testFunctionalList.stream().map(EvaluationTestExecutionTraceEntry::getDiscriminator).collect(Collectors.toList()),
                        refFunctionalList.stream().map(EvaluationTestExecutionTraceEntry::getDiscriminator).collect(Collectors.toList()))) {
                    return EvaluationTestExecutionStatus.of(evaluationTestExecution, EvaluationTestExecutionStatusTypes.CORRECT_BEHAVIOR, descriptionCorrect);
                } else {
                    return EvaluationTestExecutionStatus.of(evaluationTestExecution, EvaluationTestExecutionStatusTypes.CORRECT_BEHAVIOR_BUT_DIFFERENT_CONTROL_FLOW, descriptionCorrect);
                }
            }
        }
        if (testFunctionalList.size() > 0) {
            var inErrorFilterOpt = testFunctionalList.stream().filter(f -> f.getDiscriminator().toLowerCase().trim().contains("error"));
            if (inErrorFilterOpt.count() > 0) {
                var descriptionInError = inErrorFilterOpt.findFirst().orElseThrow().getDiscriminator();
                return EvaluationTestExecutionStatus.of(evaluationTestExecution, EvaluationTestExecutionStatusTypes.IN_ERROR_STATE, descriptionInError);
            }
        }
        if (testFunctionalList.size() > 0) {
            var managedStateList = testCaseManagedStateRepository.findByTestExecution(evaluationTestExecution.getTestExecution());
            var last = Iterables.getLast(testFunctionalList);
            var descriptionIncorrect = last.getDiscriminator();
            if (managedStateList.stream().anyMatch(m -> last.getDiscriminator().contains(m.getName()))) {
                return EvaluationTestExecutionStatus.of(evaluationTestExecution, EvaluationTestExecutionStatusTypes.INCORRECT_BEHAVIOR_BUT_MANAGED_STATE,
                        descriptionIncorrect);
            } else {
                return EvaluationTestExecutionStatus.of(evaluationTestExecution, EvaluationTestExecutionStatusTypes.INCORRECT_BEHAVIOR_BUT_UNMANAGED_STATE,
                        descriptionIncorrect);
            }
        }
        return EvaluationTestExecutionStatus.of(evaluationTestExecution, EvaluationTestExecutionStatusTypes.INCONCLUSIVE, "");
    }

    @Transactional
    public EvaluationTestExecutionSuite analyzeAndGetSuite(List<EvaluationTestExecution> evaluationTestExecutionList) {
        var evaluationTestExecutionSuite = new EvaluationTestExecutionSuite();
        evaluationTestExecutionSuite.setGeneratedAt(new Date());
        evaluationTestExecutionSuiteRepository.save(evaluationTestExecutionSuite);
        for (var evaluationTestExecution : evaluationTestExecutionList) {
            EvaluationTestExecutionSuiteResult result = evaluationTestExecutionSuiteResultRepository
                    .findByEvaluationTestExecutionSuiteAndResult(evaluationTestExecutionSuite,
                            evaluationTestExecution.getResultString());
            if (result == null) {
                result = new EvaluationTestExecutionSuiteResult();
                result.setOccurrenceCount(1);
                result.setResult(evaluationTestExecution.getResultString());
                result.setEvaluationTestExecutionSuite(evaluationTestExecutionSuite);
            } else {
                result.setOccurrenceCount(result.getOccurrenceCount() + 1);
            }
            evaluationTestExecutionSuiteResultRepository.save(result);
        }
        evaluationTestExecutionSuite.setEvaluationTestExecutionList(evaluationTestExecutionList);
        evaluationTestExecutionSuiteRepository.save(evaluationTestExecutionSuite);
        return evaluationTestExecutionSuite;
    }
}
