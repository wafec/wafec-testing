package wafec.testing.support.virtualbox.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import wafec.testing.support.virtualbox.VirtualBoxMachine;

import java.util.Optional;

@Component
public class VirtualBoxCommunicationFactory {
    @Autowired
    ApplicationContext context;

    public VirtualBoxCommunicationFactory() { }

    public VirtualBoxCommunication build(VirtualBoxMachine virtualBoxMachine) {
        if (Optional.ofNullable(virtualBoxMachine.getOperatingSystem()).equals(Optional.of("linux")))
            return context.getBean(VirtualBoxCommunicationLinux.class);
        throw new UnsupportedOperationException(String.format("%s communication not implemented yet", virtualBoxMachine.getOperatingSystem()));
    }
}
