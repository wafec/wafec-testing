package wafec.testing.execution.app.commandline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import picocli.CommandLine.*;
import wafec.testing.execution.EnvironmentController;
import wafec.testing.execution.TestCase;
import wafec.testing.execution.TestCaseRepository;
import wafec.testing.execution.openstack.robustness.OpenStackRobustnessTestRunner;
import wafec.testing.execution.robustness.RobustnessTest;
import wafec.testing.execution.robustness.RobustnessTestExecution;
import wafec.testing.execution.robustness.RobustnessTestExecutionRepository;
import wafec.testing.execution.robustness.RobustnessTestRepository;
import wafec.testing.support.virtualbox.VirtualBoxController;

import java.util.concurrent.Callable;

@Command(name = "openstack")
public class TestingRobustnessOpenStack implements Callable<Integer> {
    @ArgGroup(exclusive = true)
    MutualOption group;

    static class MutualOption {
        @Option(names = { "-t", "--test-case" }, paramLabel = "TEST-CASE-ID") long testCaseId = -1;
        @Option(names = { "-r", "--robustness-test" }, paramLabel = "ROBUSTNESS-TEST-ID") long robustnessTestId = -1;
    }

    @Option(defaultValue = "false", names = { "-s", "--scan" }, paramLabel = "SCAN")
    private boolean scan;

    @Option(names = { "-e", "--environment" }, paramLabel = "ENVIRONMENT-ID")
    private long environmentId = -1;

    @Autowired
    private OpenStackRobustnessTestRunner openStackRobustnessTestRunner;
    @Autowired
    private TestCaseRepository testCaseRepository;
    @Autowired
    private RobustnessTestExecutionRepository robustnessTestExecutionRepository;
    @Autowired
    private RobustnessTestRepository robustnessTestRepository;

    @Autowired
    private ApplicationContext context;

    private Logger logger = LoggerFactory.getLogger(TestingRobustnessOpenStack.class);

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
        logger.info(String.format("BEGIN robustness-test-id=%d, test-case-id=%d, scan=%s",
                robustnessTest.getId(), robustnessTest.getTestCase().getId(), scan));
        EnvironmentController environmentController = null;
        if (environmentId != -1) {
            environmentController = context.getBean(VirtualBoxController.class, environmentId);
        }
        openStackRobustnessTestRunner.setEnvironmentController(environmentController);
        openStackRobustnessTestRunner.manage(robustnessTest, scan);
        logger.info(String.format("  END robustness-test-id=%d, test-case-id=%d, scan=%s",
                robustnessTest.getId(), robustnessTest.getTestCase().getId(), scan));
        return 0;
    }
}
