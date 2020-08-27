package wafec.testing.execution.analysis;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationTestExecutionTraceRepository extends CrudRepository<EvaluationTestExecutionTrace, Long> {
    @Query("SELECT trace FROM EvaluationTestExecutionTrace trace WHERE trace.evaluationTestExecution = ?1 AND trace.testExecutionSourceType = ?2")
    List<EvaluationTestExecutionTrace> findByEvaluationTestExecutionAndTestExecutionSourceType(EvaluationTestExecution evaluationTestExecution,
                                                                                               TestExecutionSourceTypes testExecutionSourceType);
}
