package wafec.testing.execution.analysis;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractEvaluationPresenter {
    @Autowired
    private EvaluationTestExecutionSuiteResultRepository evaluationTestExecutionSuiteResultRepository;

    public void printResultsFor(EvaluationTestExecutionSuite evaluationTestExecutionSuite) {
        var evaluationTestExecutionSuiteResultList = evaluationTestExecutionSuiteResultRepository.findByEvaluationTestExecutionSuite(
                evaluationTestExecutionSuite
        );
        System.out.println(String.format("## Evaluation Suite #%d", evaluationTestExecutionSuite.getId()));
        System.out.println(String.format("  Generated At: %s", evaluationTestExecutionSuite.getGeneratedAt()));
        System.out.println(String.format("  Size: %d", evaluationTestExecutionSuiteResultList.size()));
        for (var evaluationTestExecutionSuiteResult : evaluationTestExecutionSuiteResultList) {
            System.out.println(String.format("  - Key String: %s", evaluationTestExecutionSuiteResult.getResult()));
            System.out.println(String.format("    Count: %d", evaluationTestExecutionSuiteResult.getOccurrenceCount()));
        }
    }
}
