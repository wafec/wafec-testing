package wafec.testing.execution.app;

import picocli.CommandLine;

@CommandLine.Command(name = "robustness", subcommands = {
        TestingRobustnessOpenStack.class
})
public class TestingRobustness {
}
