package wafec.testing.execution.robustness;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InjectionFaultRepository extends CrudRepository<InjectionFault, Long> {
    @Query("SELECT if FROM InjectionFault if, RobustnessTestExecution re WHERE if.robustnessTestExecution = re AND re.robustnessTest = ?1")
    List<InjectionFault> findByRobustnessTest(RobustnessTest robustnessTest);
    @Query("SELECT if FROM InjectionFault if, RobustnessTestExecution re WHERE if.robustnessTestExecution = re AND re.robustnessTest = ?1 AND if.used is true")
    List<InjectionFault> findByRobustnessTestAndUsed(RobustnessTest robustnessTest);
    @Query("SELECT DISTINCT concat(if.sourceKey, ' ', if.context) FROM RobustnessTestExecution re, InjectionFault if WHERE if.robustnessTestExecution = re AND re.robustnessTest = ?1")
    List<String> findByRobustnessTestDistinctSourceKeyAndContext(RobustnessTest robustnessTest);
    @Query("SELECT DISTINCT concat(if.sourceKey, ' ', if.context) FROM RobustnessTestExecution re, InjectionFault if WHERE if.robustnessTestExecution = re AND re.robustnessTest = ?1 AND if.used is true")
    List<String> findByRobustnessTestAndUsedDistinctSourceKeyAndContext(RobustnessTest robustnessTest);
    @Query("SELECT COUNT(*) FROM InjectionFault if WHERE if.robustnessTestExecution = ?1 AND if.used is true")
    long countByRobustnessTestExecutionAndUsed(RobustnessTestExecution robustnessTestExecution);
}
