package wafec.testing.execution;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestExecutionInputRepository extends CrudRepository<TestExecutionInput, Long> {
    @Query("SELECT tei FROM TestExecutionInput tei WHERE tei.testInput = ?1 AND tei.testExecution = ?2")
    List<TestExecutionInput> findByTestInputAndTestExecution(TestInput testInput, TestExecution testExecution);
    @Query("SELECT tei FROM TestExecutionInput tei, TestInput ti WHERE tei.testExecution = ?1 AND tei.testInput = ti AND ti.important is true")
    List<TestExecutionInput> findByTestExecutionAndImportant(TestExecution testExecution);
    @Query("SELECT tei FROM TestExecutionInput tei WHERE tei.testExecution = ?1")
    List<TestExecutionInput> findByTestExecution(TestExecution testExecution);
    @Query("SELECT tei FROM TestExecutionInput tei WHERE tei.testExecution = :testExecution AND tei.startedAt IS NOT NULL AND tei.endedAt IS NOT NULL ")
    List<TestExecutionInput> findByTestExecutionAndStartedAndEndedAtAreNotNull(@Param("testExecution") TestExecution testExecution);
    @Query("SELECT tei FROM TestExecutionInput tei LEFT JOIN FETCH tei.testInput WHERE tei.testExecution = ?1 AND tei.startedAt IS NOT NULL ORDER BY tei.testInput.position")
    List<TestExecutionInput> findByTestExecutionAndStartedAtIsNotNull(TestExecution testExecution);
}
