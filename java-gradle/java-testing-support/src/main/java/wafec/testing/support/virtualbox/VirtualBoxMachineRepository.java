package wafec.testing.support.virtualbox;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirtualBoxMachineRepository extends CrudRepository<VirtualBoxMachine, Long> {
    @Query("SELECT vm FROM VirtualBoxMachine vm WHERE vm.virtualBoxMachineGroup = ?1")
    List<VirtualBoxMachine> findByVirtualBoxMachineGroup(VirtualBoxMachineGroup virtualBoxMachineGroup);
    @Query("SELECT vm FROM VirtualBoxMachine vm WHERE vm.name = ?1")
    VirtualBoxMachine findByName(String name);
}
