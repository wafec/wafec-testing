package wafec.testing.execution.app.commandline;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import wafec.testing.execution.app.commandline.models.sch.SchConfig;
import wafec.testing.execution.utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "sch")
public class TestingSch implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "CONFIGURATION-FILE")
    File configurationFile;

    @Autowired
    SchOutputCommandGroupRepository schOutputCommandGroupRepository;
    @Autowired
    SchOutputCommandRepository schOutputCommandRepository;
    @Autowired
    SchOutputCommandSetRepository schOutputCommandSetRepository;

    Logger logger = LoggerFactory.getLogger(TestingSch.class);

    @Override
    public Integer call() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        var config = mapper.readValue(configurationFile, SchConfig.class);
        List<SchOutputCommandGroup> schOutputCommandGroups = new ArrayList<>();
        for (var group : config.getGroups()) {
            SchOutputCommandGroup commandGroup;
            if (group.getId() != null) {
                commandGroup = schOutputCommandGroupRepository.findById(group.getId()).orElseThrow(IllegalArgumentException::new);
            } else {
                commandGroup = new SchOutputCommandGroup();
            }
            schOutputCommandGroups.add(commandGroup);
            commandGroup.setHost(group.getHost());
            commandGroup.setUsername(group.getUsername());
            commandGroup.setPasswd(group.getPassword());
            schOutputCommandGroupRepository.save(commandGroup);
            group.setId(commandGroup.getId());
            for (var cmd : group.getCommands()) {
                SchOutputCommand command;
                if (cmd.getId() != null) {
                    command = schOutputCommandRepository.findById(cmd.getId()).orElseThrow(IllegalArgumentException::new);
                } else {
                    command = new SchOutputCommand();
                }
                command.setCommand(cmd.getCommand());
                command.setPriority(cmd.getPriority());
                command.setSuppress(cmd.getSuppress());
                command.setIgnoreIfInvalid(cmd.getIgnoreIfInvalid());
                command.setIgnoreOnError(cmd.getIgnoreOnError());
                command.setSource(cmd.getSource());
                command.setDatePattern(cmd.getDatePattern());
                command.setGroup(commandGroup);
                schOutputCommandRepository.save(command);
                cmd.setId(command.getId());
            }
        }

        SchOutputCommandSet schOutputCommandSet = null;
        if (config.getCommandSetId() == null)
            schOutputCommandSet = new SchOutputCommandSet();
        else
            schOutputCommandSet = schOutputCommandSetRepository.findById(config.getCommandSetId()).orElseGet(SchOutputCommandSet::new);
        schOutputCommandSet.setSchOutputCommandGroups(schOutputCommandGroups);
        schOutputCommandSetRepository.save(schOutputCommandSet);

        mapper.writerWithDefaultPrettyPrinter().writeValue(configurationFile, config);

        System.out.println("Succeed!");

        return 0;
    }
}
