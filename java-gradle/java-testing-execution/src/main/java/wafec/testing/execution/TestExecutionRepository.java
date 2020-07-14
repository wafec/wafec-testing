package wafec.testing.execution;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestExecutionRepository extends CrudRepository<TestExecution, Long> {
    List<TestExecution> findByTestCase(TestCase testCase);
}
