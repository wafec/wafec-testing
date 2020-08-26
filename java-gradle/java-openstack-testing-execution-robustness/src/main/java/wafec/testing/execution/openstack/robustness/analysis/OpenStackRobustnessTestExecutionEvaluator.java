package wafec.testing.execution.openstack.robustness.analysis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wafec.testing.execution.*;
import wafec.testing.execution.analysis.*;
import wafec.testing.execution.robustness.RobustnessTestExecutionRepository;
import wafec.testing.execution.robustness.RobustnessTestExecutionService;
import wafec.testing.execution.robustness.analysis.AbstractRobustnessTestExecutionEvaluator;
import wafec.testing.execution.robustness.analysis.RobustnessEvaluationTestExecutionRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class OpenStackRobustnessTestExecutionEvaluator extends AbstractRobustnessTestExecutionEvaluator {
    @Autowired
    private TestExecutionObservedOutputRepository testExecutionObservedOutputRepository;
    @Autowired
    private EvaluationTestExecutionTraceEntryRepository testExecutionTraceEntryRepository;
    @Autowired
    private RobustnessTestExecutionService robustnessTestExecutionService;
    @Autowired
    private RobustnessEvaluationTestExecutionRepository robustnessEvaluationTestExecutionRepository;
    @Autowired
    private TestExecutionInputRepository testExecutionInputRepository;

    @Override
    protected List<EvaluationTestExecutionTrace> findTraces(final EvaluationTestExecution evaluationTestExecution) {
        var robustnessEvaluationTestExecution = robustnessEvaluationTestExecutionRepository.findByEvaluationTestExecution(evaluationTestExecution);
        List<EvaluationTestExecutionTrace> evaluationTestExecutionTraceList = new ArrayList<>();
        evaluationTestExecutionTraceList.add(createExecutionTraceByMethods(evaluationTestExecution.getTestExecution(), TestExecutionSourceTypes.TEST));
        evaluationTestExecutionTraceList.add(createExecutionTraceByStates(evaluationTestExecution.getTestExecution(), TestExecutionSourceTypes.TEST));
        evaluationTestExecutionTraceList.add(createFailureTrace(evaluationTestExecution.getTestExecution(), TestExecutionSourceTypes.TEST));
        var robustnessTestExecutionReference = robustnessTestExecutionService.findRobustnessTestExecutionReference(robustnessEvaluationTestExecution.getRobustnessTestExecution());
        if (robustnessTestExecutionReference != null) {
            evaluationTestExecutionTraceList.add(createExecutionTraceByMethods(robustnessTestExecutionReference.getTestExecution(), TestExecutionSourceTypes.REFERENCE));
            evaluationTestExecutionTraceList.add(createExecutionTraceByStates(robustnessTestExecutionReference.getTestExecution(), TestExecutionSourceTypes.REFERENCE));
        }
        return evaluationTestExecutionTraceList;
    }

    private EvaluationTestExecutionTrace createExecutionTraceByMethods(TestExecution testExecution, TestExecutionSourceTypes testExecutionSourceType) {
        var evaluationTestExecutionTrace = new EvaluationTestExecutionTrace();
        evaluationTestExecutionTrace.setTraceType(EvaluationTestExecutionTraceTypes.CONTROL);
        evaluationTestExecutionTrace.setTestExecutionSource(testExecution);
        evaluationTestExecutionTrace.setTestExecutionSourceType(testExecutionSourceType);
        evaluationTestExecutionTraceRepository.save(evaluationTestExecutionTrace);

        var testExecutionObservedOutputList = testExecutionObservedOutputRepository.findByTestExecution(testExecution);
        var evaluationTestExecutionTraceEntryList = new ArrayList<EvaluationTestExecutionTraceEntry>();
        for (var observedOutput : testExecutionObservedOutputList) {
            var testOutput = observedOutput.getTestOutput();
            if (testOutput.getOutput() == null || testOutput.getSourceType() == null)
                continue;
            if (testOutput.getOutput().trim().contains("method=") && testOutput.getSourceType().equals("test-runner")) {
                String[] pairs = testOutput.getOutput().trim().split(",");
                var methodKeyValueList = Arrays.stream(pairs).filter(p -> p.trim().startsWith("method")).collect(Collectors.toList());
                var usedMethods = methodKeyValueList.stream().map(p -> p.split("=")[1]).collect(Collectors.joining(", "));
                if (StringUtils.isNotEmpty(usedMethods)) {
                    EvaluationTestExecutionTraceEntry entry = new EvaluationTestExecutionTraceEntry();
                    entry.setDiscriminator(usedMethods);
                    entry.setTestExecutionObservedOutput(observedOutput);
                    entry.setEvaluationTestExecutionTrace(evaluationTestExecutionTrace);
                    evaluationTestExecutionTraceEntryList.add(entry);
                }
            }
        }
        evaluationTestExecutionTraceEntryRepository.saveAll(evaluationTestExecutionTraceEntryList);

        return evaluationTestExecutionTrace;
    }

    private EvaluationTestExecutionTrace createExecutionTraceByStates(TestExecution testExecution, TestExecutionSourceTypes testExecutionSourceType) {
        var evaluationTestExecutionTrace = new EvaluationTestExecutionTrace();
        evaluationTestExecutionTrace.setTraceType(EvaluationTestExecutionTraceTypes.FUNCTIONAL);
        evaluationTestExecutionTrace.setTestExecutionSource(testExecution);
        evaluationTestExecutionTrace.setTestExecutionSourceType(testExecutionSourceType);
        evaluationTestExecutionTraceRepository.save(evaluationTestExecutionTrace);

        var testExecutionObservedOutputList = testExecutionObservedOutputRepository.findByTestExecution(testExecution);
        List<EvaluationTestExecutionTraceEntry> evaluationTestExecutionTraceEntryList = new ArrayList<>();
        for (var observedOutput : testExecutionObservedOutputList) {
            var testOutput = observedOutput.getTestOutput();
            if (testOutput.getOutput() == null || testOutput.getSourceType() == null)
                continue;
            if (testOutput.getOutput().trim().startsWith("Name:") && testOutput.getSourceType().equals("change")) {
                var entry = new EvaluationTestExecutionTraceEntry();
                entry.setDiscriminator(testOutput.getOutput());
                entry.setEvaluationTestExecutionTrace(evaluationTestExecutionTrace);
                entry.setTestExecutionObservedOutput(observedOutput);
                evaluationTestExecutionTraceEntryList.add(entry);
            }
        }
        evaluationTestExecutionTraceEntryRepository.saveAll(evaluationTestExecutionTraceEntryList);

        return evaluationTestExecutionTrace;
    }

    private EvaluationTestExecutionTrace createFailureTrace(TestExecution testExecution, TestExecutionSourceTypes testExecutionSourceType) {
        final var evaluationTestExecutionTrace = new EvaluationTestExecutionTrace();
        evaluationTestExecutionTrace.setTestExecutionSourceType(testExecutionSourceType);
        evaluationTestExecutionTrace.setTraceType(EvaluationTestExecutionTraceTypes.FAILURE);
        evaluationTestExecutionTrace.setTestExecutionSource(testExecution);
        evaluationTestExecutionTraceRepository.save(evaluationTestExecutionTrace);

        var inputs = testExecutionInputRepository.findByTestExecutionAndStartedAtIsNotNull(testExecution);
        if (inputs.size() > 0) {
            var dateStartRef = inputs.get(0).getStartedAt();
            var dateEndRef = testExecution.getEndTime();
            var importantListOpt = inputs.stream().filter(i -> i.getTestInput().isImportant());
            if (importantListOpt.count() > 0) {
                var importantFirst = importantListOpt.min(Comparator.comparing(TestExecutionInput::getStartedAt)).orElseThrow();
                dateStartRef = importantFirst.getStartedAt();
            }
            var schObservedOutputList = testExecutionObservedOutputRepository.findByTestExecutionAndSchLogsBetweenSpecificDate(
                    testExecution, dateStartRef, dateEndRef
            );
            final var evaluationTestExecutionTraceEntryList = new ArrayList<EvaluationTestExecutionTraceEntry>();
            schObservedOutputList.stream().filter(o -> StringUtils.isNotEmpty(o.getTestOutput().getOutput()) &&
                    o.getTestOutput().getOutput().contains("ERROR")).forEach(o -> {
                EvaluationTestExecutionTraceEntry entry = new EvaluationTestExecutionTraceEntry();
                entry.setTestExecutionObservedOutput(o);
                entry.setDiscriminator(o.getTestOutput().getOutput());
                entry.setEvaluationTestExecutionTrace(evaluationTestExecutionTrace);
                evaluationTestExecutionTraceEntryList.add(entry);
            });
            evaluationTestExecutionTraceEntryRepository.saveAll(evaluationTestExecutionTraceEntryList);
        }

        return evaluationTestExecutionTrace;
    }
}
