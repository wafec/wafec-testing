package wafec.testing.execution.utils;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchOutputCommandGroupRepository extends CrudRepository<SchOutputCommandGroup, Long> {
}
