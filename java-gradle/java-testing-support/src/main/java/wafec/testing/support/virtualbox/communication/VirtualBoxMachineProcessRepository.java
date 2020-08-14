package wafec.testing.support.virtualbox.communication;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wafec.testing.support.virtualbox.VirtualBoxMachine;

import java.util.List;

@Repository
public interface VirtualBoxMachineProcessRepository extends CrudRepository<VirtualBoxMachineProcess, Long> {
    @Query("SELECT process FROM VirtualBoxMachineProcess process WHERE process.virtualBoxMachine = ?1 AND process.processType = ?2")
    List<VirtualBoxMachineProcess> findByVirtualBoxMachineAndProcessType(VirtualBoxMachine virtualBoxMachine,
                                                                         String processType);
    @Query("SELECT process FROM VirtualBoxMachineProcess process WHERE process.virtualBoxMachine = ?1 AND process.processName = ?2")
    VirtualBoxMachineProcess findByVirtualBoxMachineAndProcessName(VirtualBoxMachine virtualBoxMachine,
                                                                   String processName);
}
