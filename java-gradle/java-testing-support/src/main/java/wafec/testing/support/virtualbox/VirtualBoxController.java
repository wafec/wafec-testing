package wafec.testing.support.virtualbox;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wafec.testing.execution.EnvironmentController;
import wafec.testing.execution.EnvironmentException;
import wafec.testing.support.virtualbox.communication.VirtualBoxCommunication;
import wafec.testing.support.virtualbox.communication.VirtualBoxCommunicationFactory;
import wafec.testing.support.virtualbox.communication.VirtualBoxMachineProcess;
import wafec.testing.support.virtualbox.communication.VirtualBoxMachineProcessRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class VirtualBoxController implements EnvironmentController {
    @Value("${wafec.testing.support.virtualbox.exec:#{null}}")
    private String exec;

    private final long groupId;

    @Autowired
    private VirtualBoxMachineRepository virtualBoxMachineRepository;
    @Autowired
    private VirtualBoxMachineGroupRepository virtualBoxMachineGroupRepository;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private VirtualBoxCommunicationFactory virtualBoxCommunicationFactory;
    private VirtualBoxMachineProcessRepository virtualBoxMachineProcessRepository;

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
        var streamHandler = new PumpStreamHandler(null, null, null);
        for (var machine : machines) {
            String powerOffLine = String.format("%s controlvm %s poweroff", getExecCmd(), machine.getName());
            CommandLine commandLine = CommandLine.parse(powerOffLine);
            DefaultExecutor executor = new DefaultExecutor();
            ExecuteWatchdog watchdog = new ExecuteWatchdog(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES));
            executor.setWatchdog(watchdog);
            executor.setExitValues(new int[] { 0, 1 });
            executor.setStreamHandler(streamHandler);
            executor.execute(commandLine);
            logger.info(String.format("%s stopped", machine.getName()));
            machine.setInUse(false);
            virtualBoxMachineRepository.save(machine);
        }
    }

    @Override
    public void setUp() throws EnvironmentException {
        logger.debug("Start");
        var group = virtualBoxMachineGroupRepository.findById(groupId).orElseThrow(() -> new EnvironmentException("Group not found"));
        var machines = virtualBoxMachineRepository.findByVirtualBoxMachineGroup(group);
        var streamHandler = new PumpStreamHandler(null, null, null);
        try {
            stopVms(machines);
            for (var machine : machines) {
                String restoreLine = String.format("%s snapshot %s restore %s", getExecCmd(), machine.getName(), machine.getSnapshot());
                CommandLine commandLine = CommandLine.parse(restoreLine);
                DefaultExecutor executor = new DefaultExecutor();
                ExecuteWatchdog watchdog = new ExecuteWatchdog(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES));
                executor.setWatchdog(watchdog);
                executor.setStreamHandler(streamHandler);
                executor.execute(commandLine);
                logger.info(String.format("%s restored to snapshot %s", machine.getName(), machine.getSnapshot()));
            }
            ExecutorService executorService = Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors()
            );
            List<Future<Integer>> futureList = new ArrayList<>();
            for (var machine : machines) {
                VirtualMachineStartWorker worker = context.getBean(VirtualMachineStartWorker.class,
                        machine.getId(), getExecCmd());
                var future = executorService.submit(worker);
                futureList.add(future);
            }
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.MINUTES);
            if (!executorService.isTerminated() ||
                futureList.stream().anyMatch(f -> !f.isDone())) {
                throw new EnvironmentException("Could not start VMs");
            }
            Thread.sleep(TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS));
        } catch (IOException | InterruptedException exc) {
            throw new EnvironmentException(exc.getMessage(), exc);
        } finally {
            logger.debug("End");
        }
    }

    @Override
    public void tearDown() {
        logger.debug("Start");
        try {
            Thread.sleep(TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS));
            stopVms();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            logger.debug("End");
        }
    }

    @Override
    public List<String> getNodeNames() throws EnvironmentException {
        var group = virtualBoxMachineGroupRepository.findById(groupId).orElseThrow(()-> new EnvironmentException("Group not found"));
        var machines = virtualBoxMachineRepository.findByVirtualBoxMachineGroup(group);
        return machines.stream().map(VirtualBoxMachine::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getServiceProcessNames(String nodeName) throws EnvironmentException {
        var machine = virtualBoxMachineRepository.findByName(nodeName);
        if (machine == null)
            throw new EnvironmentException(String.format("Machine %s not found", nodeName));
        var serviceProcesses = virtualBoxMachineProcessRepository.findByVirtualBoxMachineAndProcessType(machine, "service");
        return serviceProcesses.stream().map(VirtualBoxMachineProcess::getName).collect(Collectors.toList());
    }

    @Override
    public void shutdownNode(String nodeName) throws EnvironmentException {
        var machine = virtualBoxMachineRepository.findByName(nodeName);
        if (machine == null)
            throw new EnvironmentException(String.format("Machine %s not found", nodeName));
        var communication = virtualBoxCommunicationFactory.build(machine);
        if (communication.shutdown(machine) == 0) {
            logger.debug(String.format("Machine %s shutdown succeed", machine.getName()));
        } else {
            logger.error(String.format("Could not shutdown machine %s", machine.getName()));
            throw new EnvironmentException(String.format("Machine %s shutdown failed", machine.getName()));
        }
    }

    @Override
    public void stopServiceProcess(String nodeName, String serviceProcessName) throws EnvironmentException {
        var machine = virtualBoxMachineRepository.findByName(nodeName);
        if (machine == null)
            throw new EnvironmentException(String.format("Machine %s not found", nodeName));
        var communication = virtualBoxCommunicationFactory.build(machine);
        var process = virtualBoxMachineProcessRepository.findByVirtualBoxMachineAndProcessName(machine, serviceProcessName);
        if (process == null)
            throw new EnvironmentException(String.format("Service %s not found", serviceProcessName));
        if (communication.stopService(process) == 0) {
            logger.debug(String.format("Stop service %s of node %s succeed", serviceProcessName, nodeName));
        } else {
            logger.error(String.format("Could not stop service %s of node %s", serviceProcessName, nodeName));
            throw new EnvironmentException(String.format("Could not stop service %s of node %s", serviceProcessName, nodeName));
        }
    }
}
