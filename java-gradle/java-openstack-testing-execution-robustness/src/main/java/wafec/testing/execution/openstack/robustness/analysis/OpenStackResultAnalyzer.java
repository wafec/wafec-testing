package wafec.testing.execution.openstack.robustness.analysis;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.execution.TestExecutionObservedOutput;
import wafec.testing.execution.TestExecutionObservedOutputRepository;
import wafec.testing.execution.robustness.InjectionFaultRepository;
import wafec.testing.execution.robustness.RobustnessTestExecution;
import wafec.testing.execution.robustness.RobustnessTestExecutionRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenStackResultAnalyzer {
    @Autowired
    private RobustnessTestExecutionRepository robustnessTestExecutionRepository;
    @Autowired
    private TestExecutionObservedOutputRepository testExecutionObservedOutputRepository;
    @Autowired
    private InjectionFaultRepository injectionFaultRepository;

    public AnalysisByRobustnessTestExecutionResult analyzeRobustnessTestExecution(RobustnessTestExecution robustnessTestExecution,
                                                                                  List<String> modeledStateNameList) {
        var analysisResult = buildInitialAnalysisResult(robustnessTestExecution);
        analyzeByRobustnessTestExecutionReference(analysisResult);
        evaluateResults(analysisResult, modeledStateNameList);
        return analysisResult;
    }

    private AnalysisByRobustnessTestExecutionResult buildInitialAnalysisResult(RobustnessTestExecution robustnessTestExecution) {
        var testExecution = robustnessTestExecution.getTestExecution();
        var observedOutputList = testExecutionObservedOutputRepository.findByTestExecution(testExecution);
        var injectionFaultList = injectionFaultRepository.findByRobustnessTestExecution(robustnessTestExecution);
        var methodTrace = findMethodExecutionTrace(observedOutputList);
        var stateTrace = findStateExecutionTrace(observedOutputList);
        var analysisResult = new AnalysisByRobustnessTestExecutionResult();
        analysisResult.setRobustnessTestExecution(robustnessTestExecution);
        analysisResult.setInjectionSucceed(injectionFaultList.stream().anyMatch(i -> i.isUsed() && i.getInjectionTargetOperator() != null));
        analysisResult.setExecutionTrace(methodTrace);
        analysisResult.setStateTrace(stateTrace);
        analysisResult.setSucceed(observedOutputList.stream()
                .allMatch(o -> StringUtils.isEmpty(o.getTestOutput().getSourceType()) || !o.getTestOutput().getSourceType().equals("error")));
        return analysisResult;
    }

    private void analyzeByRobustnessTestExecutionReference(AnalysisByRobustnessTestExecutionResult analysisResult) {
        var injectionFaultList = injectionFaultRepository.findByRobustnessTestExecution(analysisResult.getRobustnessTestExecution());
        analysisResult.setInjectionSucceed(injectionFaultList.stream().anyMatch(i -> i.isUsed() && i.getInjectionTargetOperator() != null));

        var robustnessTestExecutionRefList = robustnessTestExecutionRepository.findByRobustnessTestAndScanIsTrue(analysisResult.getRobustnessTestExecution().getRobustnessTest());
        if (robustnessTestExecutionRefList != null && robustnessTestExecutionRefList.size() > 0) {
            robustnessTestExecutionRefList.sort(Comparator.comparing(m -> m.getTestExecution().getEndTime()));
            var robustnessTestExecutionRef = robustnessTestExecutionRefList.get(robustnessTestExecutionRefList.size() - 1);
            analysisResult.setRobustnessTestExecutionRef(robustnessTestExecutionRef);
            var observedOutputListRef = testExecutionObservedOutputRepository.findByTestExecution(robustnessTestExecutionRef.getTestExecution());
            var methodTraceRef = findMethodExecutionTrace(observedOutputListRef);
            var stateTraceRef = findStateExecutionTrace(observedOutputListRef);
            analysisResult.setExecutionTraceRef(methodTraceRef);
            analysisResult.setStateTraceRef(stateTraceRef);
        }
    }

    private void evaluateResults(AnalysisByRobustnessTestExecutionResult analysisResult, List<String> modeledStateNameList) {
        analysisResult.setStatus(OpenStackStateDestinationTypes.INCONCLUSIVE);
        if (analysisResult.getExecutionTraceRef() != null &&
            analysisResult.getStateTrace().size() > 0 && analysisResult.getStateTraceRef().size() > 0) {
            if (analysisResult.getStateTrace().get(analysisResult.getStateTrace().size() - 1).equals(
                    analysisResult.getStateTraceRef().get(analysisResult.getStateTraceRef().size() - 1)
            )) {
                analysisResult.setStatus(OpenStackStateDestinationTypes.CORRECT_STATE);
                if (!ListUtils.isEqualList(analysisResult.getStateTrace(), analysisResult.getStateTraceRef())) {
                    analysisResult.setStatus(OpenStackStateDestinationTypes.CORRECT_STATE_BUT_DIFFERENT_PATH);
                }
                analysisResult.setStatusDescription(String.format("%s = %s",
                        analysisResult.getStateTrace().get(analysisResult.getStateTrace().size() - 1),
                        analysisResult.getStateTraceRef().get(analysisResult.getStateTraceRef().size() - 1)));
            }
        }
        if (analysisResult.getStatus().equals(OpenStackStateDestinationTypes.INCONCLUSIVE)) {
            if (analysisResult.getStateTrace().stream().anyMatch(s -> s.contains("error"))) {
                analysisResult.setStatus(OpenStackStateDestinationTypes.ERROR_STATE);
                analysisResult.setStatusDescription(analysisResult.getStateTrace().stream().filter(s -> s.contains("error")).findAny().orElse("?"));
            }
        }
        if (analysisResult.getStatus().equals(OpenStackStateDestinationTypes.INCONCLUSIVE) &&
            analysisResult.getStateTrace().size() > 0) {
            var lastState = analysisResult.getStateTrace().get(analysisResult.getStateTrace().size() - 1);
            if (modeledStateNameList.stream().anyMatch(stateName -> lastState.contains(stateName.trim()))) {
                analysisResult.setStatus(OpenStackStateDestinationTypes.INCORRECT_BUT_A_MODELED_STATE);
            } else {
                analysisResult.setStatus(OpenStackStateDestinationTypes.INCORRECT_BUT_A_NOT_MODELED_STATE);
            }
            analysisResult.setStatusDescription(analysisResult.getStateTrace().get(analysisResult.getStateTrace().size() - 1));
        }
    }

    private List<String> findMethodExecutionTrace(List<TestExecutionObservedOutput> observedOutputList) {
        List<String> trace = new ArrayList<>();
        for (var observedOutput : observedOutputList) {
            var testOutput = observedOutput.getTestOutput();
            if (testOutput.getOutput() == null || testOutput.getSourceType() == null)
                continue;
            if (testOutput.getOutput().trim().contains("method=") && testOutput.getSourceType().equals("test-runner")) {
                String[] pairs = testOutput.getOutput().trim().split(",");
                var methodKeyValueList = Arrays.stream(pairs).filter(p -> p.trim().startsWith("method")).collect(Collectors.toList());
                var usedMethods = methodKeyValueList.stream().map(p -> p.split("=")[1]).collect(Collectors.joining(", "));
                if (StringUtils.isNotEmpty(usedMethods))
                    trace.add(usedMethods);
            }
        }
        return trace;
    }

    private List<String> findStateExecutionTrace(List<TestExecutionObservedOutput> observedOutputList) {
        List<String> trace = new ArrayList<>();
        for (var observedOutput : observedOutputList) {
            var testOutput = observedOutput.getTestOutput();
            if (testOutput.getOutput() == null || testOutput.getSourceType() == null)
                continue;
            if (testOutput.getOutput().trim().startsWith("Name:") && testOutput.getSourceType().equals("change")) {
                trace.add(testOutput.getOutput());
            }
        }
        return trace;
    }
}
