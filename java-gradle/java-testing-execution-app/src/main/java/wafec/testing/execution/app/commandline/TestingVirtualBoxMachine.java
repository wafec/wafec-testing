package wafec.testing.execution.app.commandline;

import picocli.CommandLine;

@CommandLine.Command(name = "machine", subcommands = {
        TestingVirtualBoxMachineCreate.class
})
public class TestingVirtualBoxMachine {
}
