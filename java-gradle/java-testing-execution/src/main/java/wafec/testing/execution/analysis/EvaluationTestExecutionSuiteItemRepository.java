package wafec.testing.execution.analysis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationTestExecutionSuiteItemRepository extends CrudRepository<EvaluationTestExecutionSuiteItem, Long> {
}
