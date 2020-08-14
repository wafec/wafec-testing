package wafec.testing.execution.utils;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchOutputCommandSetRepository extends CrudRepository<SchOutputCommandSet, Long> {
}
