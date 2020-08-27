package wafec.testing.execution.robustness;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wafec.testing.execution.TestExecution;

import java.util.Date;
import java.util.List;

@Repository
public interface RobustnessTestExecutionRepository extends CrudRepository<RobustnessTestExecution, Long> {
    @Query("SELECT te FROM RobustnessTestExecution re, TestExecution te WHERE re.robustnessTest = ?1 AND re.testExecution = te")
    List<TestExecution> findTestExecutionByRobustnessTest(RobustnessTest robustnessTest);
    @Query("SELECT rte FROM RobustnessTestExecution rte WHERE rte.robustnessTest = ?1")
    List<RobustnessTestExecution> findByRobustnessTest(RobustnessTest robustnessTest);
    @Query("SELECT rte FROM RobustnessTestExecution rte LEFT JOIN FETCH rte.testExecution WHERE rte.robustnessTest = :robustnessTest AND rte.scan is true ORDER BY rte.id")
    List<RobustnessTestExecution> findByRobustnessTestAndScanIsTrue(@Param("robustnessTest") RobustnessTest robustnessTest);
    @Query("SELECT rte FROM RobustnessTestExecution rte LEFT JOIN FETCH rte.testExecution WHERE rte.robustnessTest = :robustnessTest AND rte.scan is false ORDER BY rte.id")
    List<RobustnessTestExecution> findByRobustnessTestAndScanIsFalse(@Param("robustnessTest") RobustnessTest robustnessTest);
    @Query("SELECT rte FROM RobustnessTestExecution rte WHERE rte.testExecution = ?1")
    RobustnessTestExecution findByTestExecution(TestExecution testExecution);
}
