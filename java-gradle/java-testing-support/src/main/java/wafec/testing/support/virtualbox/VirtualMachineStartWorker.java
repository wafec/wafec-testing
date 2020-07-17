package wafec.testing.support.virtualbox;

import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class VirtualMachineStartWorker implements Callable<Integer> {
    @Autowired
    private VirtualBoxMachineRepository virtualBoxMachineRepository;

    private final long machineId;
    private final String execCmd;

    Logger logger = LoggerFactory.getLogger(VirtualMachineStartWorker.class);

    public VirtualMachineStartWorker(long machineId, String execCmd) {
        this.machineId = machineId;
        this.execCmd = execCmd;
    }

    @Override
    public Integer call() throws Exception {
        var machine = virtualBoxMachineRepository.findById(machineId).orElseThrow();
        String startVmLine = String.format("%s startvm %s --type headless", execCmd, machine.getName());
        ExecuteStreamHandler streamHandler = new PumpStreamHandler(null, null, null);
        CommandLine commandLine = CommandLine.parse(startVmLine);
        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES));
        executor.setWatchdog(watchdog);
        executor.setStreamHandler(streamHandler);
        executor.execute(commandLine);
        logger.info(String.format("%s started", machine.getName()));
        machine.setInUse(true);
        virtualBoxMachineRepository.save(machine);
        return 0;
    }
}
