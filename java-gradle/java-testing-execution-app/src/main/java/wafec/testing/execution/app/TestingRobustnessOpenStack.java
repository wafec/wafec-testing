package wafec.testing.execution.app;

import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine.*;
import wafec.testing.execution.TestCase;
import wafec.testing.execution.TestCaseRepository;
import wafec.testing.execution.openstack.robustness.OpenStackRobustnessTestRunner;

import java.util.concurrent.Callable;

@Command(name = "openstack")
public class TestingRobustnessOpenStack implements Callable<Integer> {
    @Parameters(paramLabel = "TEST-CASE-ID")
    private long testCaseId;

    @Autowired
    private OpenStackRobustnessTestRunner openStackRobustnessTestRunner;
    @Autowired
    private TestCaseRepository testCaseRepository;

    @Override
    public Integer call() throws Exception {
        TestCase testCase = testCaseRepository.findById(testCaseId).orElseThrow(IllegalArgumentException::new);
        openStackRobustnessTestRunner.manage(testCase);
        return 0;
    }
}
