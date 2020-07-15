package wafec.testing.execution.robustness;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobustnessTestRepository extends CrudRepository<RobustnessTest, Long> {
}
