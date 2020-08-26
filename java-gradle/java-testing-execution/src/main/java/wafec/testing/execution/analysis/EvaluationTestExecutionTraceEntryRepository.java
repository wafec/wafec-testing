package wafec.testing.execution.analysis;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationTestExecutionTraceEntryRepository extends CrudRepository<EvaluationTestExecutionTraceEntry, Long> {
    @Query("SELECT entry FROM EvaluationTestExecutionTraceEntry entry LEFT JOIN FETCH entry.testExecutionObservedOutput LEFT JOIN FETCH entry.evaluationTestExecutionTrace WHERE entry.evaluationTestExecutionTrace = ?1")
    List<EvaluationTestExecutionTraceEntry> findByEvaluationTestExecutionTrace(EvaluationTestExecutionTrace evaluationTestExecutionTrace);
    @Query("SELECT entry FROM EvaluationTestExecutionTraceEntry entry LEFT JOIN FETCH entry.testExecutionObservedOutput LEFT JOIN FETCH entry.evaluationTestExecutionTrace WHERE entry.evaluationTestExecutionTrace.evaluationTestExecution = ?1")
    List<EvaluationTestExecutionTraceEntry> findByEvaluationTestExecution(EvaluationTestExecution evaluationTestExecution);
}
