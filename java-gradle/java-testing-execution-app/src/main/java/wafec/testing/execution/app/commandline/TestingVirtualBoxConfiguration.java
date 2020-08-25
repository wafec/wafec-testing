package wafec.testing.execution.app.commandline;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import wafec.testing.execution.app.commandline.models.virtualBox.GroupConfiguration;
import wafec.testing.execution.app.commandline.models.virtualBox.MachineSystemConfiguration;
import wafec.testing.support.virtualbox.VirtualBoxMachine;
import wafec.testing.support.virtualbox.VirtualBoxMachineGroup;
import wafec.testing.support.virtualbox.VirtualBoxMachineGroupRepository;
import wafec.testing.support.virtualbox.VirtualBoxMachineRepository;
import wafec.testing.support.virtualbox.communication.VirtualBoxMachineLinux;
import wafec.testing.support.virtualbox.communication.VirtualBoxMachineLinuxRepository;
import wafec.testing.support.virtualbox.communication.VirtualBoxMachineProcess;
import wafec.testing.support.virtualbox.communication.VirtualBoxMachineProcessRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "configuration")
public class TestingVirtualBoxConfiguration implements Callable<Integer> {
    @CommandLine.Option(names = { "-m", "--main" }, required = true)
    File configurationFile;

    @Autowired
    VirtualBoxMachineGroupRepository virtualBoxMachineGroupRepository;
    @Autowired
    VirtualBoxMachineRepository virtualBoxMachineRepository;
    @Autowired
    VirtualBoxMachineProcessRepository virtualBoxMachineProcessRepository;
    @Autowired
    VirtualBoxMachineLinuxRepository virtualBoxMachineLinuxRepository;

    @Override
    public Integer call() throws Exception {
        System.out.println("Configuration Saved");
        parseConfiguration();
        System.out.println("Configuration Succeed");
        return 0;
    }

    GroupConfiguration parseGroupConfiguration() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(configurationFile, GroupConfiguration.class);
    }

    void saveGroupConfiguration(GroupConfiguration groupConfiguration) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.writerWithDefaultPrettyPrinter().writeValue(configurationFile, groupConfiguration);
    }

    void parseConfiguration() throws IOException {
        var groupConfiguration = parseGroupConfiguration();
        try {
            var group = virtualBoxMachineGroupRepository.findById(Optional.ofNullable(groupConfiguration.getId()).orElse(0L)).orElseGet(VirtualBoxMachineGroup::new);
            group.setProgramCmd(groupConfiguration.getProgramCmd());
            group.setGroupName(groupConfiguration.getName());
            virtualBoxMachineGroupRepository.save(group);
            System.out.println(String.format("Group ID %d Saved", group.getId()));
            groupConfiguration.setId(group.getId());
            if (groupConfiguration.getMachines() != null)
                for (var machineConfiguration : groupConfiguration.getMachines()) {
                    var machine = virtualBoxMachineRepository.findById(Optional.ofNullable(machineConfiguration.getId()).orElse(0L)).orElseGet(VirtualBoxMachine::new);
                    machine.setInUse(machine.isInUse());
                    machine.setSnapshot(machineConfiguration.getSnapshot());
                    machine.setName(machineConfiguration.getName());
                    machine.setShutdownPrevent(machineConfiguration.isShutdownPrevent());
                    machine.setVirtualBoxMachineGroup(group);
                    machine.setOperatingSystem(machineConfiguration.getOperatingSystem());
                    virtualBoxMachineRepository.save(machine);
                    System.out.println(String.format("Machine ID %d Saved", machine.getId()));
                    machineConfiguration.setId(machine.getId());
                    if (machineConfiguration.getProcesses() != null)
                        for (var processConfiguration : machineConfiguration.getProcesses()) {
                            if (virtualBoxMachineProcessRepository.findByVirtualBoxMachineAndProcessName(machine, processConfiguration.getName()) != null) {
                                System.out.println(String.format("WARN!! Please do not enter duplicate process name: %s", processConfiguration.getName()));
                                continue;
                            }
                            var process = virtualBoxMachineProcessRepository.findById(Optional.ofNullable(processConfiguration.getId()).orElse(0L)).orElseGet(VirtualBoxMachineProcess::new);
                            process.setName(processConfiguration.getName());
                            process.setVirtualBoxMachine(machine);
                            process.setProcessType(processConfiguration.getProcessType());
                            virtualBoxMachineProcessRepository.save(process);
                            System.out.println(String.format("Process ID %d Saved", process.getId()));
                            processConfiguration.setId(process.getId());
                        }
                    if (machine.getOperatingSystem() != null && machineConfiguration.getSystems() != null) {
                        for (var systemConfiguration : machineConfiguration.getSystems()) {
                            if (machine.getOperatingSystem().equals("linux")) {
                                configureLinux(machine, systemConfiguration);
                            }
                        }
                    }
                }
        } finally {
            saveGroupConfiguration(groupConfiguration);
        }
    }

    void configureLinux(VirtualBoxMachine machine, MachineSystemConfiguration systemConfiguration) {
        if (virtualBoxMachineLinuxRepository.findByVirtualBoxMachineAndAddress(machine, systemConfiguration.getAddress()) != null) {
            System.out.println(String.format("WARN!! Please do not enter duplicate system address: %s", systemConfiguration.getAddress()));
            return;
        }

        var system = virtualBoxMachineLinuxRepository.findById(Optional.ofNullable(systemConfiguration.getId()).orElse(0L)).orElseGet(VirtualBoxMachineLinux::new);
        system.setUsername(systemConfiguration.getUsername());
        system.setAddress(systemConfiguration.getAddress());
        system.setVirtualBoxMachine(machine);
        system.setPasswd(systemConfiguration.getPassword());
        virtualBoxMachineLinuxRepository.save(system);
        System.out.println(String.format("Machine Linux ID %d Saved", system.getId()));
        systemConfiguration.setId(system.getId());
    }
}
