package wafec.testing.execution.openstack.robustness.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.execution.analysis.*;
import wafec.testing.execution.robustness.analysis.RobustnessEvaluationTestExecution;

@Component
public class OpenStackRobustnessEvaluationPresenter extends AbstractEvaluationPresenter {
    @Autowired
    private EvaluationTestExecutionTraceRepository evaluationTestExecutionTraceRepository;

    public void printResultsFor(RobustnessEvaluationTestExecution robustnessEvaluationTestExecution) {
        var evaluationTestExecution = robustnessEvaluationTestExecution.getEvaluationTestExecution();
        System.out.println(String.format("## Evaluation #%d", evaluationTestExecution.getId()));
        System.out.println(String.format("  Test Execution ID: %d", evaluationTestExecution.getTestExecution().getId()));
        System.out.println(String.format("  Test ID: %d", evaluationTestExecution.getTestExecution().getTestCase().getId()));
        System.out.println(String.format("  Generated At: %s", evaluationTestExecution.getGeneratedAt()));
        System.out.println(String.format("  Pass Succeed: %b", evaluationTestExecution.isPassSucceed()));
        System.out.println(String.format("  With Textual Errors Present: %b", evaluationTestExecution.isWithTextErrorPresent()));
        System.out.println(String.format("  Reference Trace Present: %b", isReferencePresentInTraceList(evaluationTestExecution)));
        System.out.println(String.format("  Test Trace Present: %b", isTestPresentInTraceList(evaluationTestExecution)));
        System.out.println(String.format("## Robustness Evaluation #%d", robustnessEvaluationTestExecution.getId()));
        System.out.println(String.format("  Injection Succeed: %b", robustnessEvaluationTestExecution.isInjectionSucceed()));
        System.out.println(String.format("  Status: %s", evaluationTestExecution.getEvaluationTestExecutionStatus().getEvaluationTestExecutionStatusType()));
        System.out.println("  Description:");
        System.out.println("  -----------------------------------------");
        System.out.println(String.format("  %s", evaluationTestExecution.getEvaluationTestExecutionStatus().getDescription()));
        System.out.println("  -----------------------------------------");
    }

    private boolean isReferencePresentInTraceList(EvaluationTestExecution evaluationTestExecution) {
        var result = evaluationTestExecutionTraceRepository.findByEvaluationTestExecutionAndTestExecutionSourceType(evaluationTestExecution,
                TestExecutionSourceTypes.REFERENCE);
        return result.size() > 0;
    }

    private boolean isTestPresentInTraceList(EvaluationTestExecution evaluationTestExecution) {
        var result = evaluationTestExecutionTraceRepository.findByEvaluationTestExecutionAndTestExecutionSourceType(evaluationTestExecution,
                TestExecutionSourceTypes.TEST);
        return result.size() > 0;
    }
}
