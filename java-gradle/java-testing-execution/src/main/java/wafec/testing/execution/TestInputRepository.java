package wafec.testing.execution;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestInputRepository extends CrudRepository<TestInput, Long> {
    List<TestInput> findByTestCase(TestCase testCase);
}
