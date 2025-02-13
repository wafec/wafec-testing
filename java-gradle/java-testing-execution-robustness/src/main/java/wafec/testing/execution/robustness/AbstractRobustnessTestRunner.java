package wafec.testing.execution.robustness;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import wafec.testing.execution.*;
import wafec.testing.execution.utils.SchOutputCommandGroup;
import wafec.testing.execution.utils.SchOutputService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class AbstractRobustnessTestRunner {
    @Getter
    protected final AbstractTestDriver testDriver;
    protected final DataInterception dataInterception;
    protected final DataParser dataParser;

    @Autowired
    private TestExecutionRepository testExecutionRepository;
    @Autowired
    private RobustnessTestRepository robustnessTestRepository;
    @Autowired
    private RobustnessTestExecutionRepository robustnessTestExecutionRepository;

    protected RobustnessTestExecution currentRobustnessTestExecution;
    @Setter
    protected EnvironmentController environmentController;
    @Setter
    protected List<SchOutputCommandGroup> schOutputCommandGroupList;
    @Autowired
    protected SchOutputService schOutputService;

    Logger logger = LoggerFactory.getLogger(AbstractRobustnessTestRunner.class);

    public AbstractRobustnessTestRunner(AbstractTestDriver testDriver,
                                        DataInterception dataInterception,
                                        DataParser dataParser) {
        this.testDriver = testDriver;
        this.dataInterception = dataInterception;
        this.dataParser = dataParser;
    }

    public RobustnessTestExecution manage(RobustnessTest robustnessTest, boolean scan) throws
            TestDriverException,
            TestDataValueNotFoundException,
            PreConditionViolationException,
            RobustnessException {
        RobustnessTestExecution robustnessTestExecution = new RobustnessTestExecution();
        robustnessTestExecution.setRobustnessTest(robustnessTest);
        TestExecution testExecution = new TestExecution();
        testExecution.setStartTime(new Date());
        testExecution.setTestCase(robustnessTest.getTestCase());
        testExecutionRepository.save(testExecution);
        robustnessTestExecution.setTestExecution(testExecution);
        robustnessTestExecution.setScan(scan);
        robustnessTestExecutionRepository.save(robustnessTestExecution);
        return this.manage(robustnessTestExecution);
    }

    public RobustnessTestExecution manage(RobustnessTestExecution robustnessTestExecution) throws
            TestDriverException,
            TestDataValueNotFoundException,
            PreConditionViolationException,
            RobustnessException {
        var robustnessTest = robustnessTestExecution.getRobustnessTest();
        logger.info(String.format("Start #%d, Execution #%d", robustnessTest.getId(), robustnessTestExecution.getId()));
        var stopWatch = new StopWatch();
        stopWatch.start();
        currentRobustnessTestExecution = robustnessTestExecution;
        try {
            if (environmentController != null)
                environmentController.setUp();
            dataInterception.turnOn(this::handleDataIntercepted);
            var testExecution = testDriver.runTest(robustnessTestExecution.getTestExecution());
            robustnessTestExecution.setTestExecution(testExecution);
            robustnessTestExecutionRepository.save(robustnessTestExecution);
            return robustnessTestExecution;
        } catch (EnvironmentException exc) {
            throw new RobustnessException(exc.getMessage(), exc);
        } finally {
            dataInterception.turnOff();
            schOutputService.executeAndParse(schOutputCommandGroupList, robustnessTestExecution.getTestExecution());
            if (environmentController != null) {
                environmentController.tearDown();
            }
            stopWatch.stop();
            logger.info(String.format("End #%d %s, Execution #%d", robustnessTest.getId(), stopWatch.toString(), robustnessTestExecution.getId()));
        }
    }

    protected abstract ApplicationData adulterate(ApplicationData appData,
                                                  RobustnessTestExecution robustnessTestExecution) throws
            CouldNotApplyOperatorException;

    protected DataOperation handleDataIntercepted(byte[] data) {
        ApplicationData applicationData = null;
        try {
            applicationData = dataParser.handle(data);
            var applicationDataResult = adulterate(applicationData, currentRobustnessTestExecution);
            if (!Arrays.equals(data, applicationDataResult.getData())) {
                return DataOperation.of(new Date(), applicationData, applicationDataResult, DataOperation.NOT_SAME);
            } else {
                return DataOperation.of(new Date(), applicationData, applicationDataResult, DataOperation.IGNORED);
            }
        } catch (CouldNotApplyOperatorException exc) {
            exc.printStackTrace();
            return DataOperation.of(new Date(), applicationData, applicationData, DataOperation.CANNOT_APPLY);
        } catch (DataParseException exc) {
            exc.printStackTrace();
            return DataOperation.of(new Date(), ApplicationData.of(data), ApplicationData.of(data), DataOperation.ERROR);
        }
    }
}
