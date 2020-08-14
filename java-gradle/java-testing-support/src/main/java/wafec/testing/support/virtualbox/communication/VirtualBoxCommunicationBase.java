package wafec.testing.support.virtualbox.communication;

import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import wafec.testing.support.virtualbox.VirtualBoxMachine;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class VirtualBoxCommunicationBase implements VirtualBoxCommunication {
    @Autowired
    private VirtualBoxMachineProcessRepository virtualBoxMachineProcessRepository;

    Logger logger = LoggerFactory.getLogger(VirtualBoxCommunicationBase.class);

    @Override
    public Integer shutdown(VirtualBoxMachine machine) {
        CommandLine commandLine = CommandLine.parse(String.format("%s controlvm %s poweroff", machine.getVirtualBoxMachineGroup().getProgramCmd(),
                machine.getName()));
        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES));
        executor.setWatchdog(watchdog);
        var streamHandler = new PumpStreamHandler(null, null, null);
        executor.setStreamHandler(streamHandler);
        executor.setExitValues(new int[] { 0, 1 });
        try {
            return executor.execute(commandLine);
        } catch (IOException exc) {
            logger.error(exc.getMessage(), exc);
            return 1;
        }
    }

    @Override
    public List<VirtualBoxMachineProcess> getServiceProcessList(VirtualBoxMachine virtualBoxMachine) {
        return virtualBoxMachineProcessRepository.findByVirtualBoxMachineAndProcessType(virtualBoxMachine, "service");
    }
}
