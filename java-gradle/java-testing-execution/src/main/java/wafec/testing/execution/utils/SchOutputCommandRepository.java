package wafec.testing.execution.utils;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchOutputCommandRepository extends CrudRepository<SchOutputCommand, Long> {
    @Query("SELECT c FROM SchOutputCommand c WHERE c.group = ?1 ORDER BY c.priority")
    List<SchOutputCommand> findBySchOutputCommandGroup(SchOutputCommandGroup group);
}
