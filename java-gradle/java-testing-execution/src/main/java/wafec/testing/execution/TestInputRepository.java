package wafec.testing.execution;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestInputRepository extends CrudRepository<TestInput, Long> {
    @Query("SELECT ti FROM TestInput ti WHERE ti.testCase = ?1 ORDER BY ti.position")
    List<TestInput> findByTestCase(TestCase testCase);
}
