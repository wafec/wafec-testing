package wafec.testing.execution.app.commandline;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "injection", subcommands = {
        TestingRobustnessInjectionSummary.class,
        TestingRobustnessInjectionTarget.class
})
public class TestingRobustnessInjection {
}
