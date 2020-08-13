package wafec.testing.execution.app.commandline;

import picocli.CommandLine.*;

@Command(name = "target", subcommands = {
        TestingRobustnessInjectionTargetDiscard.class
})
public class TestingRobustnessInjectionTarget {
}
