package wafec.testing.execution.app.commandline;

import picocli.CommandLine;

@CommandLine.Command(name = "virtual-box", subcommands = {
        TestingVirtualBoxMachine.class,
        TestingVirtualBoxGroup.class
})
public class TestingVirtualBox {
}
