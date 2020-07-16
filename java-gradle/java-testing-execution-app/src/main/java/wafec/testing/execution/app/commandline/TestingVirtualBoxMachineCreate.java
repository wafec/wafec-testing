package wafec.testing.execution.app.commandline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import wafec.testing.support.virtualbox.VirtualBoxMachine;
import wafec.testing.support.virtualbox.VirtualBoxMachineGroupRepository;
import wafec.testing.support.virtualbox.VirtualBoxMachineRepository;

import java.util.Optional;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "create")
public class TestingVirtualBoxMachineCreate implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "GROUP-ID")
    private long groupId;
    @CommandLine.Parameters(paramLabel = "NAME")
    private String name;
    @CommandLine.Parameters(paramLabel = "SNAPSHOT")
    private String snapshot;

    @Autowired
    private VirtualBoxMachineGroupRepository virtualBoxMachineGroupRepository;
    @Autowired
    private VirtualBoxMachineRepository virtualBoxMachineRepository;

    Logger logger = LoggerFactory.getLogger(TestingVirtualBoxMachineCreate.class);

    @Override
    public Integer call() throws Exception {
        var group = virtualBoxMachineGroupRepository.findById(groupId).orElseThrow(IllegalArgumentException::new);
        var machine = new VirtualBoxMachine();
        machine.setVirtualBoxMachineGroup(group);
        machine.setName(Optional.of(name).orElseThrow());
        machine.setSnapshot(Optional.of(snapshot).orElseThrow());
        virtualBoxMachineRepository.save(machine);
        logger.info(String.format("Created %s", machine.toString()));
        System.out.println(machine.toString());
        return 0;
    }
}
