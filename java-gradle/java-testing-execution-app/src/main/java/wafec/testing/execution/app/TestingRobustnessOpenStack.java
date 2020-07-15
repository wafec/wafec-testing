package wafec.testing.execution.app;

import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine.*;
import wafec.testing.execution.TestCase;
import wafec.testing.execution.TestCaseRepository;
import wafec.testing.execution.openstack.robustness.OpenStackRobustnessTestRunner;
import wafec.testing.execution.robustness.RobustnessTest;
import wafec.testing.execution.robustness.RobustnessTestExecution;
import wafec.testing.execution.robustness.RobustnessTestExecutionRepository;
import wafec.testing.execution.robustness.RobustnessTestRepository;

import java.util.concurrent.Callable;

@Command(name = "openstack")
public class TestingRobustnessOpenStack implements Callable<Integer> {
    @ArgGroup(exclusive = true)
    MutualOption group;

    static class MutualOption {
        @Option(names = { "-t", "--test-case" }) long testCaseId = -1;
        @Option(names = { "-r", "--robustness-test" }) long robustnessTestId = -1;
    }

    @Autowired
    private OpenStackRobustnessTestRunner openStackRobustnessTestRunner;
    @Autowired
    private TestCaseRepository testCaseRepository;
    @Autowired
    private RobustnessTestExecutionRepository robustnessTestExecutionRepository;
    @Autowired
    private RobustnessTestRepository robustnessTestRepository;

    @Override
    public Integer call() throws Exception {
        TestCase testCase;
        RobustnessTest robustnessTest;
        if (group.testCaseId != -1) {
            testCase = testCaseRepository.findById(group.testCaseId).orElseThrow(IllegalArgumentException::new);
            robustnessTest = new RobustnessTest();
            robustnessTest.setTestCase(testCase);
            robustnessTestRepository.save(robustnessTest);
        } else if (group.robustnessTestId != -1) {
            robustnessTest = robustnessTestRepository.findById(group.robustnessTestId).orElseThrow(IllegalArgumentException::new);
        } else {
            throw new IllegalStateException();
        }
        System.out.println(String.format("RobustnessTest: %d, TestCase: %d", robustnessTest.getId(), robustnessTest.getTestCase().getId()));
        openStackRobustnessTestRunner.manage(robustnessTest);
        return 0;
    }
}
