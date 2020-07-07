package wafec.testing.execution.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import wafec.testing.execution.TestCaseRepository;
import wafec.testing.execution.openstack.OpenStackTestDriver;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "openstack")
@Component
public class TestingExecutionOpenStack implements Callable<Integer> {
    @CommandLine.Parameters(paramLabel = "TEST-CASE-ID")
    private long testCaseId;

    @Autowired
    private OpenStackTestDriver openStackTestDriver;
    @Autowired
    private TestCaseRepository testCaseRepository;

    @Override
    public Integer call() throws Exception {
        var testCase = testCaseRepository.findById(testCaseId).orElseThrow(IllegalArgumentException::new);
        openStackTestDriver.runTest(testCase);
        return 0;
    }
}
