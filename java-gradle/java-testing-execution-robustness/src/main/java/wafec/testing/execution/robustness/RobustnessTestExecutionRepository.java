package wafec.testing.execution.robustness;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wafec.testing.execution.TestExecution;

import java.util.Date;
import java.util.List;

@Repository
public interface RobustnessTestExecutionRepository extends CrudRepository<RobustnessTestExecution, Long> {
    @Query("SELECT te FROM RobustnessTestExecution re, TestExecution te WHERE re.robustnessTest = ?1 AND re.testExecution = te")
    List<TestExecution> findTestExecutionByRobustnessTest(RobustnessTest robustnessTest);
}
