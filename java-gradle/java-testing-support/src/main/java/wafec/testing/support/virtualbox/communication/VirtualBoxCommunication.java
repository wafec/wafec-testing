package wafec.testing.support.virtualbox.communication;

import wafec.testing.support.virtualbox.VirtualBoxMachine;

import java.util.List;

public interface VirtualBoxCommunication {
    Integer stopService(VirtualBoxMachineProcess process);
    Integer shutdown(VirtualBoxMachine machine);
    List<VirtualBoxMachineProcess> getServiceProcessList(VirtualBoxMachine machine);
}
