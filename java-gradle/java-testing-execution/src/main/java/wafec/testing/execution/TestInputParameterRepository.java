package wafec.testing.execution;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestInputParameterRepository extends CrudRepository<TestInputParameter, Long> {
    @Query("SELECT tp FROM TestInputParameter tp, TestInput ti WHERE ti = tp.testInput AND ti.testCase = ?1 ORDER BY ti.position")
    List<TestInputParameter> findByTestCase(TestCase testCase);
}
