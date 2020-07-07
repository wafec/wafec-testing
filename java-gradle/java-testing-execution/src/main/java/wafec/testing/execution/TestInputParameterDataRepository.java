package wafec.testing.execution;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestInputParameterDataRepository extends CrudRepository<TestInputParameterData, Long> {
    @Query("SELECT pd FROM TestInputParameterData pd, TestInputParameter p WHERE p.testInput = ?1 and p = pd.testInputParameter")
    List<TestInputParameterData> findByTestInput(TestInput testInput);
}
