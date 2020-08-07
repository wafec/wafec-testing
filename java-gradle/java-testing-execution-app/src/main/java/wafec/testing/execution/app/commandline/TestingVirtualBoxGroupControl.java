package wafec.testing.execution.app.commandline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import picocli.CommandLine;
import wafec.testing.support.virtualbox.VirtualBoxController;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "control")
public class TestingVirtualBoxGroupControl implements Callable<Integer> {
    @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
    Exclusive exclusive;

    static class Exclusive {
        @CommandLine.Option(names = { "-s", "--set-up" }, required = true) boolean setUp;
        @CommandLine.Option(names = { "-t", "--tear-down" }, required = true) boolean tearDown;
    }

    @CommandLine.Option(names = { "-g", "--group-id" }, required = true)
    long groupId;

    @Autowired
    ApplicationContext appContext;

    @Override
    public Integer call() throws Exception {
        VirtualBoxController controller = appContext.getBean(VirtualBoxController.class, groupId);
        if (exclusive.setUp)
            controller.setUp();
        else if (exclusive.tearDown)
            controller.tearDown();
        return 0;
    }
}
