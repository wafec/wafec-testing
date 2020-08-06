package wafec.testing.execution.robustness;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InjectionTargetManagedRepository extends CrudRepository<InjectionTargetManaged, Long> {
    @Query("SELECT itm FROM InjectionTargetManaged itm WHERE itm.injectionTarget = ?1")
    List<InjectionTargetManaged> findByInjectionTarget(InjectionTarget injectionTarget);
    //@Query("SELECT * FROM ")
    //long countRemainingInUseManagedTotalByRobustnessTest(RobustnessTest robustnessTest);
}
