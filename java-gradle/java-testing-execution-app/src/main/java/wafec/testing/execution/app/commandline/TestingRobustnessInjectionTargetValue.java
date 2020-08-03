package wafec.testing.execution.app.commandline;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "value")
public class TestingRobustnessInjectionTargetValue implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {

        return 0;
    }
}
