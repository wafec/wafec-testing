package wafec.testing.execution.app.commandline;

import picocli.CommandLine;

@CommandLine.Command(name = "robustness", subcommands = {
        TestingRobustnessOpenStack.class,
        TestingRobustnessInjection.class
})
public class TestingRobustness {
}
