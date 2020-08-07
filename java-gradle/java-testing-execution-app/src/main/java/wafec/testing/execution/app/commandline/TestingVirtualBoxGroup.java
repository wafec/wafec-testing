package wafec.testing.execution.app.commandline;

import picocli.CommandLine;

@CommandLine.Command(name = "group", subcommands = {
        TestingVirtualBoxGroupCreate.class,
        TestingVirtualBoxGroupControl.class
})
public class TestingVirtualBoxGroup {
}
