package wafec.testing.execution.robustness;

import org.springframework.beans.factory.annotation.Autowired;
import wafec.testing.execution.*;

import java.util.Date;

public abstract class AbstractRobustnessTestRunner {
    protected final AbstractTestDriver testDriver;
    protected final DataInterception dataInterception;
    protected final DataCorruption dataCorruption;
    protected final DataHandler dataHandler;

    @Autowired
    private TestExecutionRepository testExecutionRepository;

    public AbstractRobustnessTestRunner(AbstractTestDriver testDriver,
                                        DataInterception dataInterception,
                                        DataCorruption dataCorruption,
                                        DataHandler dataHandler) {
        this.testDriver = testDriver;
        this.dataInterception = dataInterception;
        this.dataCorruption = dataCorruption;
        this.dataHandler = dataHandler;
    }

    public TestExecution manage(TestCase testCase) throws
            TestDriverException,
            TestDataValueNotFoundException,
            PreConditionViolationException,
            RobustnessException {
        TestExecution testExecution = TestExecution.of(testCase);
        testExecutionRepository.save(testExecution);
        return this.manage(testExecution);
    }

    public TestExecution manage(TestExecution testExecution) throws
            TestDriverException,
            TestDataValueNotFoundException,
            PreConditionViolationException,
            RobustnessException {
        try {
            dataInterception.turnOn(this::onDataIntercepted);
            return testDriver.runTest(testExecution);
        } finally {
            dataInterception.turnOff();
        }
    }

    protected DataOperation onDataIntercepted(byte[] raw) {
        ApplicationData applicationData = null;
        try {
            applicationData = dataHandler.handle(raw);
            var applicationDataResult = dataCorruption.corrupt(applicationData);
            if (applicationData.compareChange(applicationDataResult)) {
                return DataOperation.of(new Date(), applicationData, applicationDataResult, DataOperation.MUTATED);
            } else {
                return DataOperation.of(new Date(), applicationData, applicationDataResult, DataOperation.IGNORED);
            }
        } catch (CouldNotApplyOperatorException exc) {
            return DataOperation.of(new Date(), null, applicationData, DataOperation.CANNOT_APPLY);
        } catch (DataParseException exc) {
            return DataOperation.of(new Date(), null, ApplicationData.ofRaw(raw), DataOperation.ERROR);
        }
    }
}
