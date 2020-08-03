package wafec.testing.core;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisBuildJsonObjectRepository extends CrudRepository<AnalysisBuildJsonObject, Long> {
}
