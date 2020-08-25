package wafec.testing.execution.robustness;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InjectionTargetOperatorRepository extends CrudRepository<InjectionTargetOperator, Long> {
    @Query("SELECT io FROM InjectionTargetOperator io WHERE io.injectionTarget = ?1")
    List<InjectionTargetOperator> findByInjectionTarget(InjectionTarget injectionTarget);
    @Query("SELECT io FROM InjectionTargetOperator io WHERE io.injectionTarget.robustnessTest = ?1 AND io.injectionTarget.sourceKey = ?2 AND io.ignored is false")
    List<InjectionTargetOperator> findByRobustnessTestAndSourceKey(RobustnessTest robustnessTest, String sourceKey);
    @Query("SELECT io FROM InjectionTargetOperator io WHERE io.injectionTarget.robustnessTest = ?1 AND io.injectionTarget.sourceKey = ?2 AND io.used is false AND io.ignored is false")
    List<InjectionTargetOperator> findByRobustnessTestAndSourceKeyAndNotUsed(RobustnessTest robustnessTest, String sourceKey);
}
