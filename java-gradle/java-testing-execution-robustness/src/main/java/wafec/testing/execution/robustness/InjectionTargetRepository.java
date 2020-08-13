package wafec.testing.execution.robustness;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wafec.testing.execution.robustness.models.SourceKeyCount;

import java.util.List;

@Repository
public interface InjectionTargetRepository extends CrudRepository<InjectionTarget, Long> {
    @Query("SELECT it FROM InjectionTarget it WHERE it.sourceKey = ?1 AND it.context = ?2 AND it.robustnessTest = ?3")
    List<InjectionTarget> findBySourceKeyAndContextAndRobustnessTest(String sourceKey, String context, RobustnessTest robustnessTest);
    @Query("SELECT it FROM InjectionTarget it WHERE it.sourceKey = ?1 AND it.robustnessTest = ?2")
    List<InjectionTarget> findBySourceKeyAndRobustessTest(String sourceKey, RobustnessTest robustnessTest);
    @Query("SELECT it FROM InjectionTarget it WHERE it.robustnessTest = ?1")
    List<InjectionTarget> findByRobustnessTest(RobustnessTest robustnessTest);
    @Query("SELECT it.sourceKey AS sourceKey, count(*) AS sourceKeyCount FROM InjectionTarget it WHERE it.robustnessTest = ?1 AND it.discard is true GROUP BY it.sourceKey")
    List<SourceKeyCount> findSourceKeysByRobustnessTestAndDiscardIsTrue(RobustnessTest robustnessTest);
    @Query("SELECT it.sourceKey AS sourceKey, count(*) AS sourceKeyCount FROM InjectionTarget it WHERE it.robustnessTest = ?1 AND it.discard is false GROUP BY it.sourceKey")
    List<SourceKeyCount> findSourceKeysByRobustnessTestAndDiscardIsFalse(RobustnessTest robustnessTest);
}
