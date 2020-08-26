package wafec.testing.execution.robustness.analysis;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wafec.testing.execution.analysis.EvaluationTestExecution;

@Repository
public interface RobustnessEvaluationTestExecutionRepository extends CrudRepository<RobustnessEvaluationTestExecution, Long> {
    @Query("SELECT r FROM RobustnessEvaluationTestExecution r WHERE r.evaluationTestExecution = ?1")
    RobustnessEvaluationTestExecution findByEvaluationTestExecution(EvaluationTestExecution evaluationTestExecution);
}
