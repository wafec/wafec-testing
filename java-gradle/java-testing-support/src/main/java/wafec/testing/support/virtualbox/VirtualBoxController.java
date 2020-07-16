package wafec.testing.support.virtualbox;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wafec.testing.execution.EnvironmentController;
import wafec.testing.execution.EnvironmentException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class VirtualBoxController implements EnvironmentController {
    @Value("${wafec.testing.support.virtualbox.exec:#{null}}")
    private String exec;

    private final long groupId;

    @Autowired
    private VirtualBoxMachineRepository virtualBoxMachineRepository;
    @Autowired
    private VirtualBoxMachineGroupRepository virtualBoxMachineGroupRepository;

    private Logger logger = LoggerFactory.getLogger(VirtualBoxController.class);

    public VirtualBoxController(long groupId) {
        this.groupId = groupId;
    }

    private String getExecCmd() throws EnvironmentException {
        if (StringUtils.isNotEmpty(exec))
            return exec;
        var group = virtualBoxMachineGroupRepository.findById(groupId);
        if (group.isPresent()) {
            return group.get().getProgramCmd();
        }
        throw new EnvironmentException("No group or exec found");
    }

    private void stopVms() throws EnvironmentException, IOException {
        var group = virtualBoxMachineGroupRepository.findById(groupId).orElseThrow(() -> new EnvironmentException("Group not found"));
        var machines = virtualBoxMachineRepository.findByVirtualBoxMachineGroup(group);
        stopVms(machines);
    }

    private void stopVms(List<VirtualBoxMachine> machines) throws IOException, EnvironmentException {
        for (var machine : machines) {
            String powerOffLine = String.format("%s controlvm %s poweroff", getExecCmd(), machine.getName());
            CommandLine commandLine = CommandLine.parse(powerOffLine);
            DefaultExecutor executor = new DefaultExecutor();
            ExecuteWatchdog watchdog = new ExecuteWatchdog(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES));
            executor.setWatchdog(watchdog);
            executor.setExitValue(1);
            executor.execute(commandLine);
            logger.info(String.format("%s stopped", machine.getName()));
            machine.setInUse(false);
            virtualBoxMachineRepository.save(machine);
        }
    }

    @Override
    public void setUp() throws EnvironmentException {
        var group = virtualBoxMachineGroupRepository.findById(groupId).orElseThrow(() -> new EnvironmentException("Group not found"));
        var machines = virtualBoxMachineRepository.findByVirtualBoxMachineGroup(group);
        try {
            stopVms(machines);
            for (var machine : machines) {
                String restoreLine = String.format("%s snapshot %s restore %s", getExecCmd(), machine.getName(), machine.getSnapshot());
                CommandLine commandLine = CommandLine.parse(restoreLine);
                DefaultExecutor executor = new DefaultExecutor();
                ExecuteWatchdog watchdog = new ExecuteWatchdog(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES));
                executor.setWatchdog(watchdog);
                executor.execute(commandLine);
                logger.info(String.format("%s restored to snapshot %s", machine.getName(), machine.getSnapshot()));
            }
            for (var machine : machines) {
                String startVmLine = String.format("%s startvm %s --type headless", getExecCmd(), machine.getName());
                CommandLine commandLine = CommandLine.parse(startVmLine);
                DefaultExecutor executor = new DefaultExecutor();
                ExecuteWatchdog watchdog = new ExecuteWatchdog(TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES));
                executor.setWatchdog(watchdog);
                executor.execute(commandLine);
                logger.info(String.format("%s started", machine.getName()));
                machine.setInUse(true);
                virtualBoxMachineRepository.save(machine);
            }
        } catch (IOException exc) {
            throw new EnvironmentException(exc.getMessage(), exc);
        }
    }

    @Override
    public void tearDown() {
        try {
            stopVms();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
