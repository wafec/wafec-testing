package wafec.testing.execution.analysis;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractEvaluationPresenter {
    @Autowired
    private EvaluationTestExecutionSuiteResultRepository evaluationTestExecutionSuiteResultRepository;
    @Autowired
    private EvaluationTestExecutionSuiteResultTestExecutionRepository evaluationTestExecutionSuiteResultTestExecutionRepository;

    public void printResultsFor(EvaluationTestExecutionSuite evaluationTestExecutionSuite) {
        printResultsFor(evaluationTestExecutionSuite, false, null);
    }

    public void printResultsFor(EvaluationTestExecutionSuite evaluationTestExecutionSuite, boolean expanded, String filter) {
        var evaluationTestExecutionSuiteResultList = evaluationTestExecutionSuiteResultRepository.findByEvaluationTestExecutionSuite(
                evaluationTestExecutionSuite
        );
        System.out.println(String.format("## Evaluation Suite #%d", evaluationTestExecutionSuite.getId()));
        System.out.println(String.format("  Generated At: %s", evaluationTestExecutionSuite.getGeneratedAt()));
        System.out.println(String.format("  Size: %d", evaluationTestExecutionSuiteResultList.size()));
        for (var evaluationTestExecutionSuiteResult : evaluationTestExecutionSuiteResultList) {
            System.out.println(String.format("  - Key String: %s", evaluationTestExecutionSuiteResult.getResult()));
            System.out.println(String.format("    Count: %d", evaluationTestExecutionSuiteResult.getOccurrenceCount()));
            if (expanded) {
                var evaluationTestExecutionSuiteResultTestExecutionList =
                        evaluationTestExecutionSuiteResultTestExecutionRepository.findByEvaluationTestExecutionSuiteResult(evaluationTestExecutionSuiteResult);
                for (var evaluationTestExecutionSuiteResultTestExecution : evaluationTestExecutionSuiteResultTestExecutionList) {
                    var description = evaluationTestExecutionSuiteResultTestExecution.getEvaluationTestExecution().getEvaluationTestExecutionStatus().getDescription();
                    if (filter == null || evaluationTestExecutionSuiteResult.getResult().matches(filter)) {
                        System.out.println(String.format("    - %s", description));
                        System.out.println("    -------------------------------------");
                    }
                }
            }
        }
    }
}
