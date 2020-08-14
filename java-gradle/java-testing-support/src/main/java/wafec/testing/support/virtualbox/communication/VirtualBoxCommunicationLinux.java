package wafec.testing.support.virtualbox.communication;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VirtualBoxCommunicationLinux extends VirtualBoxCommunicationBase {
    @Autowired
    private VirtualBoxMachineLinuxRepository virtualBoxMachineLinuxRepository;

    Logger logger = LoggerFactory.getLogger(VirtualBoxCommunicationLinux.class);

    @Override
    public Integer stopService(VirtualBoxMachineProcess process) {
        if (!process.getProcessType().equals("service"))
            throw new IllegalStateException("Cannot stop a non-service process");
        var machine = process.getVirtualBoxMachine();
        var linuxList = virtualBoxMachineLinuxRepository.findByVirtualBoxMachine(machine);
        if (linuxList.size() > 0) {
            var linux = linuxList.get(0);
            var jsch = new JSch();
            try {
                Session session = jsch.getSession(linux.getUsername(), linux.getAddress());
                session.setPassword(linux.getPasswd());
                session.setConfig("StringHostKeyChecking", "no");
                session.connect();
                ChannelExec channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(String.format("sudo systemctl stop %s", process.getName()));
                channel.setInputStream(null);
                channel.connect();
                while (!channel.isClosed()) {
                    try { Thread.sleep(100); } catch(Exception exc) { };
                }
                channel.disconnect();
                session.disconnect();
                return 0;
            } catch (JSchException exc) {
                logger.error(exc.getMessage(), exc);
            }
        }
        return 1;
    }
}
