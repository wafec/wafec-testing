package wafec.testing.execution.app.commandline;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import picocli.CommandLine.*;
import wafec.testing.execution.*;
import wafec.testing.execution.openstack.OpenStackTestDriverExecutionException;
import wafec.testing.execution.openstack.robustness.OpenStackRobustnessTestRunner;
import wafec.testing.execution.robustness.*;
import wafec.testing.execution.utils.SchOutputCommandGroup;
import wafec.testing.execution.utils.SchOutputCommandGroupRepository;
import wafec.testing.execution.utils.SchOutputCommandSetRepository;
import wafec.testing.support.virtualbox.VirtualBoxController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @Option(names = { "-x", "--repeat" })
    private int repeat = 1;
    @Option(names = { "-h", "--sch-group" }, paramLabel = "SCH-GROUP-ID")
    private Long[] schGroupIds;
    @Option(names = { "-c", "--sch-command-set" }, paramLabel = "SCH-COMMAND-SET" )
    private Long schCommandSetId;

    @Autowired
    private OpenStackRobustnessTestRunner openStackRobustnessTestRunner;
    @Autowired
    private TestCaseRepository testCaseRepository;
    @Autowired
    private RobustnessTestExecutionRepository robustnessTestExecutionRepository;
    @Autowired
    private RobustnessTestRepository robustnessTestRepository;
    @Autowired
    private SchOutputCommandGroupRepository schOutputCommandGroupRepository;
    @Autowired
    private SchOutputCommandSetRepository schOutputCommandSetRepository;

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
        EnvironmentController environmentController = null;
        if (environmentId != -1) {
            environmentController = context.getBean(VirtualBoxController.class, environmentId);
        }
        List<SchOutputCommandGroup> schOutputCommandGroups = new ArrayList<>();
        if (schGroupIds != null) {
            var schGroups = schOutputCommandGroupRepository.findAllById(Arrays.asList(schGroupIds));
            schOutputCommandGroups.addAll(Lists.newArrayList(schGroups));
        }
        if (schCommandSetId != null) {
            var schCommandSet = schOutputCommandSetRepository.findById(schCommandSetId).orElseThrow(IllegalArgumentException::new);
            schOutputCommandGroups.addAll(schCommandSet.getSchOutputCommandGroups());
        }
        if (schOutputCommandGroups.size() > 0) {
            openStackRobustnessTestRunner.getTestDriver().setSchOutputCommandGroups(schOutputCommandGroups);
        }
        openStackRobustnessTestRunner.setEnvironmentController(environmentController);
        if (scan)
            repeat = 1;
        for (int i = 0; i < repeat; i++) {
            logger.info(String.format("BEGIN X: %d", i + 1));
            try {
                var executionInfo = openStackRobustnessTestRunner.manage(robustnessTest, scan);
                logger.info(String.format("ID: %d, Execution: %d", executionInfo.getId(), executionInfo.getTestExecution().getId()));
            } catch (TestDriverException |
                     PreConditionViolationException exc) {
                logger.error(exc.getMessage(), exc);
            }
            logger.info(String.format("END   X: %d", i + 1));
        }
        return 0;
    }
}
