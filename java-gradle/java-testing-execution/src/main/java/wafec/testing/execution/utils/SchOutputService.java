package wafec.testing.execution.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import wafec.testing.execution.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchOutputService {
    @Autowired
    private SchOutputCommandRepository schOutputCommandRepository;
    @Autowired
    private TestExecutionInputRepository testExecutionInputRepository;
    @Autowired
    private TestExecutionObservedOutputRepository testExecutionObservedOutputRepository;
    @Autowired
    private TestOutputRepository testOutputRepository;

    private Logger logger = LoggerFactory.getLogger(SchOutputService.class);

    public List<SchOutput> execute(SchOutputCommandGroup group) {
        logger.info("Starting getting sch outputs");
        var commands = schOutputCommandRepository.findBySchOutputCommandGroup(group);
        List<SchOutput> result = new ArrayList<>();
        JSch jsch = new JSch();
        for (var command : commands) {
            logger.info(String.format("Run SCH %s", command.toString()));
            try {
                Session session = jsch.getSession(group.getUsername(), group.getHost());
                session.setPassword(group.getPasswd());
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                ChannelExec channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(command.getCommand());
                channel.setInputStream(null);
                channel.setErrStream(System.err);
                InputStream in = channel.getInputStream();
                channel.connect();
                byte[] tmp = new byte[1024];
                StringBuilder buffer = new StringBuilder();
                while (true) {
                    while (in.available() > 0) {
                        int i = in.read(tmp, 0, 1024);
                        if (i < 0) break;
                        buffer.append(new String(tmp, 0, i));
                    }
                    if (channel.isClosed()) {
                        if (in.available() > 0) continue;
                        break;
                    }
                    try { Thread.sleep(100); } catch (Exception exc) {}
                }
                channel.disconnect();
                session.disconnect();
                if (!command.isSuppress())
                    result.addAll(Arrays.stream(buffer.toString().split("\\r?\\n"))
                             .map(l -> SchOutput.of(l,
                                     new Date(),
                                     Optional.ofNullable(command.getSource()).orElse(SchOutputService.class.getName()),
                                     command.isIgnoreIfInvalid()))
                            .collect(Collectors.toList()));
                if (!StringUtils.isEmpty(command.getDatePattern())) {
                    result.forEach(schOutput -> {
                        var parts = schOutput.getLine().split("\\s");
                        if (parts.length > 1) {
                            String dateStr = parts[0] + " " + parts[1];
                            try {
                                String datePattern = "yyyy-MM-dd HH:mm:ss.SSS";
                                if (!command.getDatePattern().equals("default"))
                                    datePattern = command.getDatePattern();
                                SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
                                var date = formatter.parse(dateStr);
                                schOutput.setCreatedAt(date);
                            } catch (ParseException exc) {
                                schOutput.setCreatedAt(null);
                            }
                        }
                    });
                    if (command.isIgnoreOnError())
                        result = result.stream().filter(schOutput -> schOutput.getCreatedAt() != null)
                                .collect(Collectors.toList());
                }
            } catch (JSchException | IOException exc) {
                logger.error(exc.getMessage(), exc);
                SchOutput schErrorOutput = SchOutput.of(String.format("ERROR source=%s, command=%s, message='%s'",
                        command.getSource(), command.getCommand(), exc.getMessage()), new Date(), "sch-error", false);
                result.add(schErrorOutput);
            } catch (Exception exc) {
                logger.error(exc.getMessage(), exc);
                SchOutput schExceptionOutput = SchOutput.of(String.format("EXCEPTION source=%s, command=%s, message=%s",
                        command.getSource(), command.getCommand(), exc.getMessage()), new Date(), "sch-exception", false);
                result.add(schExceptionOutput);
            }
        }
        logger.info("SCH succeed");
        return result;
    }

    public void executeAndParse(List<SchOutputCommandGroup> schOutputCommandGroupList, TestExecution testExecution) {
        if (schOutputCommandGroupList == null || schOutputCommandGroupList.size() == 0)
            return;
        var testExecutionInputList = testExecutionInputRepository.findByTestExecutionAndStartedAndEndedAtAreNotNull(testExecution);
        for (var schOutputCommandGroup : schOutputCommandGroupList) {
            var result = this.execute(schOutputCommandGroup);
            for (int i = 0; i < result.size(); i++) {
                try {
                    var schOutput = result.get(i);

                    TestOutput testOutput = new TestOutput();
                    testOutput.setCreatedAt(schOutput.getCreatedAt());
                    testOutput.setOutput(schOutput.getLine());
                    testOutput.setSource(schOutput.getSource());
                    testOutput.setSourceType("sch");
                    testOutput.setPosition(0);

                    var testExecutionInput = testExecutionInputList
                            .stream().filter(input -> testOutput.getCreatedAt().after(input.getStartedAt())
                                    && testOutput.getCreatedAt().before(input.getEndedAt()))
                            .findFirst().orElse(null);

                    TestInput testInput = null;
                    if (testExecutionInput != null) {
                        testInput = testExecutionInput.getTestInput();
                        int maxPosition = testExecutionObservedOutputRepository.maxPositionByTestExecutionAndTestInput(testExecution, testInput);
                        testOutput.setPosition(maxPosition + 1);
                    } else if (schOutput.isIgnoreIfInvalid()) {
                        continue;
                    }

                    testOutputRepository.save(testOutput);
                    TestExecutionObservedOutput testExecutionObservedOutput =
                            TestExecutionObservedOutput.of(testExecution, testInput, testOutput);
                    testExecutionObservedOutputRepository.save(testExecutionObservedOutput);
                } catch (Exception exc) {
                    logger.error(exc.getMessage(), exc);
                }
            }
        }
    }
}
