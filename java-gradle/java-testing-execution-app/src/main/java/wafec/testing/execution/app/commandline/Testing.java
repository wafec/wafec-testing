package wafec.testing.execution.app.commandline;

import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Command(name = "testing", subcommands = {
        TestingImport.class,
        TestingExecution.class,
        TestingClean.class,
        TestingRobustness.class,
        TestingHelper.class,
        TestingVirtualBox.class
})
@Component
public class Testing {
}
