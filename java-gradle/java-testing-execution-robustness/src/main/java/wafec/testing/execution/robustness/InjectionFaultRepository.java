package wafec.testing.execution.robustness;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InjectionFaultRepository extends CrudRepository<InjectionFault, Long> {
    @Query("SELECT DISTINCT concat(it.sourceKey, ' ', it.context) FROM RobustnessTestExecution re, InjectionFault if, InjectionTarget it WHERE if.robustnessTestExecution = re AND re.robustnessTest = ?1 AND if.injectionTarget = it AND it.discard is false")
    List<String> findByRobustnessTestAndNotDiscardDistinctSourceKeyAndContext(RobustnessTest robustnessTest);
    @Query("SELECT DISTINCT concat(it.sourceKey, ' ', it.context) FROM RobustnessTestExecution re, InjectionFault if, InjectionTarget it WHERE if.robustnessTestExecution = re AND re.robustnessTest = ?1 AND if.used is true AND if.injectionTarget = it AND it.discard is false")
    List<String> findByRobustnessTestAndUsedAndNotDiscardDistinctSourceKeyAndContext(RobustnessTest robustnessTest);
    @Query("SELECT COUNT(*) FROM InjectionFault if, InjectionTarget it WHERE if.robustnessTestExecution = ?1 AND if.used is true AND if.injectionTarget = it AND it.discard is false")
    long countByRobustnessTestExecutionAndUsedAndNotDiscard(RobustnessTestExecution robustnessTestExecution);
    @Query("SELECT DISTINCT concat(it.sourceKey, ' ', it.context) FROM InjectionFault if, InjectionTarget it WHERE if.robustnessTestExecution = ?1 AND if.injectionTarget = it AND it.discard is false")
    List<String> findByRobustnessTestExecutionAndNotDiscardDistinctSourceKeyAndContext(RobustnessTestExecution robustnessTestExecution);
}
