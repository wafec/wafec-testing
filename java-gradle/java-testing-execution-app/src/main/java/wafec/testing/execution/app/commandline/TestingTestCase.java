package wafec.testing.execution.app.commandline;

import picocli.CommandLine.*;

@Command(name = "testcase", subcommands = {
        TestingTestCaseManaged.class
})
public class TestingTestCase {
}
