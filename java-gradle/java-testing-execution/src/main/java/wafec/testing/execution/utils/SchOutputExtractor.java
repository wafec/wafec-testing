package wafec.testing.execution.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SchOutputExtractor {
    @Autowired
    private SchOutputCommandRepository schOutputCommandRepository;
    private Logger logger = LoggerFactory.getLogger(SchOutputExtractor.class);

    public List<SchOutput> execute(SchOutputCommandGroup group) {
        var commands = schOutputCommandRepository.findBySchOutputCommandGroup(group);
        List<SchOutput> result = new ArrayList<>();
        JSch jsch = new JSch();
        for (var command : commands) {
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
                             .map(l -> SchOutput.of(l, new Date(), Optional.ofNullable(command.getSource()).orElse(SchOutputExtractor.class.getName()))).collect(Collectors.toList()));
            } catch (JSchException | IOException exc) {
                logger.error(exc.getMessage(), exc);
            }
        }
        return result;
    }
}
