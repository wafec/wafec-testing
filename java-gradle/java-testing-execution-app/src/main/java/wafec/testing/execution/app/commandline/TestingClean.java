package wafec.testing.execution.app.commandline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import wafec.testing.execution.app.services.TestCleanService;

import java.util.concurrent.Callable;

@Component
@CommandLine.Command(name = "clean")
public class TestingClean implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "TEST-CASE-ID")
    private long testCaseId;
    @Autowired
    private TestCleanService testCleanService;

    @Override
    public Integer call() throws Exception {
        testCleanService.clearTest(testCaseId);
        return 0;
    }


}
