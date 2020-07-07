package wafec.testing.execution;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestExecutionObservedOutputRepository extends CrudRepository<TestExecutionObservedOutput, Long> {
    @Query("SELECT o FROM TestExecutionObservedOutput o, TestExecution e WHERE o.testExecution = e AND e.testCase = ?1")
    List<TestExecutionObservedOutput> findByTestCase(TestCase testCase);
}
