package wafec.testing.execution.robustness;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobustnessTestExecutionRepository extends CrudRepository<RobustnessTestExecution, Long> {
}
