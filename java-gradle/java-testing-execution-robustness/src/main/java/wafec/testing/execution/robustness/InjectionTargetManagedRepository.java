package wafec.testing.execution.robustness;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InjectionTargetManagedRepository extends CrudRepository<InjectionTargetManaged, Long> {
    List<InjectionTargetManaged> findByInjectionTarget(InjectionTarget injectionTarget);
}
