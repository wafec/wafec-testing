package wafec.testing.execution;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestExecutionInputRepository extends CrudRepository<TestExecutionInput, Long> {
    @Query("SELECT tei FROM TestExecutionInput tei WHERE tei.testInput = ?1 AND tei.testExecution = ?2")
    List<TestExecutionInput> findByTestInputAndTestExecution(TestInput testInput, TestExecution testExecution);
    @Query("SELECT tei FROM TestExecutionInput tei, TestInput ti WHERE tei.testExecution = ?1 AND tei.testInput = ti AND ti.important is true")
    List<TestExecutionInput> findByTestExecutionAndImportant(TestExecution testExecution);
    @Query("SELECT tei FROM TestExecutionInput tei WHERE tei.testExecution = ?1")
    List<TestExecutionInput> findByTestExecution(TestExecution testExecution);
}
