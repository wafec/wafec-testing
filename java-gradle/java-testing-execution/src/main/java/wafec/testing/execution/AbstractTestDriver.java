package wafec.testing.execution;

import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public abstract class AbstractTestDriver {
    public static final String SOURCE = AbstractTestDriver.class.getName();

    @Autowired
    private TestInputRepository testInputRepository;
    @Autowired
    private TestOutputMapper testOutputMapper;
    @Autowired
    private TestExecutionObservedOutputRepository testExecutionObservedOutputRepository;
    @Autowired
    private TestExecutionRepository testExecutionRepository;
    @Autowired
    private TestOutputRepository testOutputRepository;

    protected TestDriverContext testDriverContext;

    public TestExecution runTest(TestCase testCase) throws TestDriverInputNotFoundException,
            TestDataValueNotFoundException, PreConditionViolationException, TestDriverException {
        var testExecution = TestExecution.of(testCase);
        testExecutionRepository.save(testExecution);
        return runTest(testExecution);
    }

    public TestExecution runTest(TestExecution testExecution) throws TestDriverInputNotFoundException,
            TestDataValueNotFoundException, PreConditionViolationException, TestDriverException {
        testDriverContext = new TestDriverContext(this);
        var testInputs = testInputRepository.findByTestCase(testExecution.getTestCase());
        for (var testInput : testInputs) {
            var testDriverObservedOutputList = tryRunTestInput(testInput, testExecution);
            saveObservedOutputs(testDriverObservedOutputList, testExecution, testInput);
        }
        testExecution.setEndTime(new Date());
        testExecutionRepository.save(testExecution);
        return testExecution;
    }

    private void saveObservedOutputs(List<TestDriverObservedOutput> observedOutputs, TestExecution testExecution,
                                     TestInput testInput) {
        int i = 0;
        for (var testDriverObservedOutput : observedOutputs) {
            var testOutput = testOutputMapper.fromTestDriverObservedOutput(testDriverObservedOutput);
            testOutput.setCreatedAt(new Date());
            testOutput.setPosition(i++);
            TestExecutionObservedOutput observedOutput =
                    TestExecutionObservedOutput.of(testExecution, testInput, testOutput);
            testOutputRepository.save(testOutput);
            testExecutionObservedOutputRepository.save(observedOutput);
        }
    }

    private List<TestDriverObservedOutput> tryRunTestInput(TestInput testInput, TestExecution testExecution) throws
            TestDriverInputNotFoundException, TestDataValueNotFoundException, PreConditionViolationException, TestDriverException {
        try {
            return runTestInput(testInput, testExecution);
        } catch (Exception exc) {
            var builder = new TestDriverObservedOutputBuilder();
            if (exc instanceof TestDriverException)
                builder.join(((TestDriverException) exc).getObservedOutputsOnFail());
            builder.and().error(SOURCE, exc);
            saveObservedOutputs(builder.buildList(), testExecution, testInput);
            throw exc;
        }
    }

    protected abstract List<TestDriverObservedOutput> runTestInput(TestInput testInput, TestExecution testExecution) throws TestDriverInputNotFoundException,
            TestDataValueNotFoundException, PreConditionViolationException, TestDriverException;
    public abstract List<AcceptedInput> getAcceptedInputs();
}
