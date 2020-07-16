package wafec.testing.execution.app.commandline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import wafec.testing.support.virtualbox.VirtualBoxMachineGroup;
import wafec.testing.support.virtualbox.VirtualBoxMachineGroupRepository;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "create")
public class TestingVirtualBoxGroupCreate implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "NAME")
    private String name;
    @CommandLine.Parameters(paramLabel = "PROGRAM-CMD")
    private String programCmd;

    @Autowired
    private VirtualBoxMachineGroupRepository virtualBoxMachineGroupRepository;

    Logger logger = LoggerFactory.getLogger(TestingVirtualBoxGroupCreate.class);

    @Override
    public Integer call() throws Exception {
        var group = new VirtualBoxMachineGroup();
        group.setGroupName(name);
        group.setProgramCmd(programCmd);
        virtualBoxMachineGroupRepository.save(group);
        logger.info(String.format("Created %s", group.toString()));
        System.out.println(group.toString());
        return 0;
    }
}
