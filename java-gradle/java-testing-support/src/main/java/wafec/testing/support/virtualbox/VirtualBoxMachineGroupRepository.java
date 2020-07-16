package wafec.testing.support.virtualbox;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualBoxMachineGroupRepository extends CrudRepository<VirtualBoxMachineGroup, Long> {
}
