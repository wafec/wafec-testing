package wafec.testing.execution;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestExecutionRepository extends CrudRepository<TestExecution, Long> {
    List<TestExecution> findByTestCase(TestCase testCase);
}
