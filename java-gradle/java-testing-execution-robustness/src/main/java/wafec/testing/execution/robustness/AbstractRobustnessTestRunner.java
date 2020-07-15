package wafec.testing.execution.robustness;

import org.springframework.beans.factory.annotation.Autowired;
import wafec.testing.execution.*;

import java.util.Arrays;
import java.util.Date;

public abstract class AbstractRobustnessTestRunner {
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

    public AbstractRobustnessTestRunner(AbstractTestDriver testDriver,
                                        DataInterception dataInterception,
                                        DataParser dataParser) {
        this.testDriver = testDriver;
        this.dataInterception = dataInterception;
        this.dataParser = dataParser;
    }

    public RobustnessTestExecution manage(RobustnessTest robustnessTest) throws
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
        robustnessTestExecutionRepository.save(robustnessTestExecution);
        return this.manage(robustnessTestExecution);
    }

    public RobustnessTestExecution manage(RobustnessTestExecution robustnessTestExecution) throws
            TestDriverException,
            TestDataValueNotFoundException,
            PreConditionViolationException,
            RobustnessException {
        currentRobustnessTestExecution = robustnessTestExecution;
        try {
            dataInterception.turnOn(this::handleDataIntercepted);
            var testExecution = testDriver.runTest(robustnessTestExecution.getTestExecution());
            robustnessTestExecution.setTestExecution(testExecution);
            robustnessTestExecutionRepository.save(robustnessTestExecution);
            return robustnessTestExecution;
        } finally {
            dataInterception.turnOff();
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
