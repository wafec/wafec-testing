package wafec.testing.execution.app.commandline;

import picocli.CommandLine.*;

@Command(name = "injection-target", subcommands = {
        TestingRobustnessInjectionTargetDiscard.class
})
public class TestingRobustnessInjectionTarget {
}
