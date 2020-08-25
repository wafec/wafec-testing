package wafec.testing.execution.app.commandline;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import wafec.testing.execution.openstack.robustness.analysis.OpenStackResultAnalyzer;
import wafec.testing.execution.robustness.RobustnessTestExecutionRepository;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@CommandLine.Command(name = "analyze")
public class TestingRobustnessOpenStackAnalyze implements Callable<Integer> {
    @CommandLine.ArgGroup(exclusive = true)
    Composite composite;
    @CommandLine.Option(names = { "-s", "--state-file" }, required = true)
    File stateNameListFile;

    static class Composite {
        @CommandLine.ArgGroup(exclusive = false)
        ByTestExecution byTestExecution;
        @CommandLine.ArgGroup(exclusive = false)
        ByTest byTest;
    }

    static class ByTestExecution {
        @CommandLine.Option(names = { "-e", "--robustness-test-execution" }) long robustnessTestExecutionId;
    }

    static class ByTest {
        @CommandLine.Option(names = { "-r", "--robustness-test" }) long robustnessTestId;
    }

    @Autowired
    RobustnessTestExecutionRepository robustnessTestExecutionRepository;
    @Autowired
    OpenStackResultAnalyzer openStackResultAnalyzer;

    @Override
    public Integer call() throws Exception {
        if (composite.byTestExecution != null) {
            var robustnessTestExecution = robustnessTestExecutionRepository.findById(composite.byTestExecution.robustnessTestExecutionId)
                    .orElseThrow();
            var modeledStateNameList = FileUtils.readLines(stateNameListFile).stream().map(String::trim).collect(Collectors.toList());
            var result = openStackResultAnalyzer.analyzeRobustnessTestExecution(robustnessTestExecution, modeledStateNameList);
            System.out.println(result.toString());
        }
        return 0;
    }
}
