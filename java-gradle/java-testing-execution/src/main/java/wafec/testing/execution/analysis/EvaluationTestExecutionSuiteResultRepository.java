package wafec.testing.execution.analysis;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationTestExecutionSuiteResultRepository extends CrudRepository<EvaluationTestExecutionSuiteResult, Long> {
    @Query("SELECT r FROM EvaluationTestExecutionSuiteResult r WHERE r.evaluationTestExecutionSuite = ?1 AND r.result = ?2")
    EvaluationTestExecutionSuiteResult findByEvaluationTestExecutionSuiteAndResult(EvaluationTestExecutionSuite evaluationTestExecutionSuite, String result);
    @Query("SELECT r FROM EvaluationTestExecutionSuiteResult r WHERE r.evaluationTestExecutionSuite = ?1")
    List<EvaluationTestExecutionSuiteResult> findByEvaluationTestExecutionSuite(EvaluationTestExecutionSuite evaluationTestExecutionSuite);
}
