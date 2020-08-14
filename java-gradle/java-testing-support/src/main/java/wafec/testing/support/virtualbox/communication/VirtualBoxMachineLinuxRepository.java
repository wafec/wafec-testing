package wafec.testing.support.virtualbox.communication;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wafec.testing.support.virtualbox.VirtualBoxMachine;

import java.util.List;

@Repository
public interface VirtualBoxMachineLinuxRepository extends CrudRepository<VirtualBoxMachineLinux, Long> {
    @Query("SELECT linux FROM VirtualBoxMachineLinux linux WHERE linux.virtualBoxMachine = ?1 ORDER BY linux.id DESC")
    List<VirtualBoxMachineLinux> findByVirtualBoxMachine(VirtualBoxMachine virtualBoxMachine);
}
