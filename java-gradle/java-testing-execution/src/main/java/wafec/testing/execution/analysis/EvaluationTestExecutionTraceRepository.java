package wafec.testing.execution.analysis;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationTestExecutionTraceRepository extends CrudRepository<EvaluationTestExecutionTrace, Long> {

}
