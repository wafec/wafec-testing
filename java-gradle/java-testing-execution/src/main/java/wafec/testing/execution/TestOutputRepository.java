package wafec.testing.execution;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestOutputRepository extends CrudRepository<TestOutput, Long> {
    @Query("SELECT o FROM TestOutput o, TestExecution e, TestExecutionObservedOutput eo WHERE e = ?1 AND eo.testExecution = e AND eo.testOutput = o")
    List<TestOutput> findByTestExecution(TestExecution testExecution);
    @Query("SELECT o FROM TestOutput o, TestExecution e, TestExecutionObservedOutput eo WHERE e.testCase = ?1 AND eo.testExecution = e AND eo.testOutput = o")
    List<TestOutput> findByTestCase(TestCase testCase);
}
