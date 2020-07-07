package wafec.testing.execution.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import wafec.testing.execution.*;
import wafec.testing.execution.app.services.TestClearService;

import java.util.concurrent.Callable;

@Component
@CommandLine.Command(name = "clear")
public class TestingClear implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "TEST-CASE-ID")
    private long testCaseId;
    @Autowired
    private TestClearService testClearService;

    @Override
    public Integer call() throws Exception {
        testClearService.clearTest(testCaseId);
        return 0;
    }


}
