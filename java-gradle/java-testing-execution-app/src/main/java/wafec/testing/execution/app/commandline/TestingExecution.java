package wafec.testing.execution.app.commandline;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "execution", subcommands = {
        TestingExecutionOpenStack.class
})
public class TestingExecution {
}
