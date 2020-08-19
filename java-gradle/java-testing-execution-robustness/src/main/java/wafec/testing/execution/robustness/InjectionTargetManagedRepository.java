package wafec.testing.execution.robustness;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InjectionTargetManagedRepository extends CrudRepository<InjectionTargetManaged, Long> {
    @Query("SELECT itm FROM InjectionTargetManaged itm WHERE itm.injectionTarget = ?1")
    List<InjectionTargetManaged> findByInjectionTarget(InjectionTarget injectionTarget);
    @Query("SELECT itm FROM InjectionTargetManaged itm WHERE itm.injectionTarget.robustnessTest = ?1 AND itm.injectionTarget.sourceKey = ?2")
    List<InjectionTargetManaged> findByRobustnessTestAndSourceKey(RobustnessTest robustnessTest, String sourceKey);
    @Query("SELECT itm FROM InjectionTargetManaged itm WHERE itm.injectionTarget = ?1 AND itm.injectorName = ?2")
    List<InjectionTargetManaged> findByInjectionTargetAndInjectorName(InjectionTarget injectionTarget, String injectorName);
    @Query("SELECT itm FROM InjectionTargetManaged itm WHERE itm.injectionTarget.robustnessTest = ?1 AND itm.injectionTarget.sourceKey = ?2 AND itm.injectorName = ?3")
    List<InjectionTargetManaged> findByRobustnessTestAndSourceKeyAndInjectorName(RobustnessTest robustnessTest, String sourceKey, String injectorName);
    @Query("SELECT count (*) FROM InjectionTarget it, InjectionTargetManaged im WHERE it.robustnessTest = ?1 AND im.injectionTarget = it AND im.injectionCount = 0 AND it.discard is false")
    long countByRobustnessTestAndInjectionCountEqualToZero(RobustnessTest robustnessTest);
    @Query("SELECT count (*) FROM InjectionTarget it, InjectionTargetManaged im WHERE it.robustnessTest = ?1 AND im.injectionTarget = it AND im.injectionCount > 0 AND it.discard is false")
    long countByRobustnessTestAndInjectionCountGreaterThanZero(RobustnessTest robustnessTest);
}
