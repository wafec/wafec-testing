package wafec.testing.execution;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestInputParameterDataRepository extends CrudRepository<TestInputParameterData, Long> {
    @Query("SELECT pd FROM TestInputParameterData pd, TestInputParameter p WHERE p.testInput = ?1 AND p = pd.testInputParameter")
    List<TestInputParameterData> findByTestInput(TestInput testInput);
    @Query("SELECT pd FROM TestInputParameterData pd, TestInputParameter ip, TestInput ti WHERE pd.testInputParameter = ip AND ip.testInput = ti AND ti.testCase = ?1 ORDER BY ti.position")
    List<TestInputParameterData> findByTestCase(TestCase testCase);
}
