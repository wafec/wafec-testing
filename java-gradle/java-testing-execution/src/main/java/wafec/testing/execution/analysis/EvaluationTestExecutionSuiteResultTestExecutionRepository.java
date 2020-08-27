package wafec.testing.execution.analysis;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationTestExecutionSuiteResultTestExecutionRepository extends CrudRepository<EvaluationTestExecutionSuiteResultTestExecution, Long> {
    @Query("SELECT r FROM EvaluationTestExecutionSuiteResultTestExecution r LEFT JOIN FETCH r.evaluationTestExecution WHERE r.evaluationTestExecutionSuiteResult = ?1")
    List<EvaluationTestExecutionSuiteResultTestExecution> findByEvaluationTestExecutionSuiteResult(EvaluationTestExecutionSuiteResult evaluationTestExecutionSuiteResult);
}
