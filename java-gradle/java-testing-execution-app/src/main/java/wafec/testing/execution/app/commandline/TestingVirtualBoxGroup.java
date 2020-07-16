package wafec.testing.execution.app.commandline;

import picocli.CommandLine;

@CommandLine.Command(name = "group", subcommands = {
        TestingVirtualBoxGroupCreate.class
})
public class TestingVirtualBoxGroup {
}
