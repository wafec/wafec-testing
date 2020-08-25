package wafec.testing.execution.robustness;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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
    @Modifying
    @Query("UPDATE InjectionTarget it SET it.discard = true WHERE it.robustnessTest = :robustnessTest AND (it.context LIKE '%.name' OR it.context LIKE '%.namespace' OR it.context LIKE '%.version')")
    void deactivateNotTargetingDataInjections(@Param("robustnessTest") RobustnessTest robustnessTest);
    @Query("SELECT COUNT(*) FROM InjectionTarget it WHERE it.robustnessTest = ?1 AND (it.context LIKE '%.name' OR it.context LIKE '%.namespace' OR it.context LIKE '%.version')")
    long countByNotTargetingDataInjections(RobustnessTest robustnessTest);
}
