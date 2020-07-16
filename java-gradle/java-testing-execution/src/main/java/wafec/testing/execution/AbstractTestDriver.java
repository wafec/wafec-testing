package wafec.testing.execution;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Autowired
    private TestExecutionInputRepository testExecutionInputRepository;

    protected TestDriverContext testDriverContext;
    @Setter
    protected EnvironmentController environmentController;

    public TestExecution runTest(TestCase testCase) throws
            TestDataValueNotFoundException, PreConditionViolationException, TestDriverException {
        var testExecution = TestExecution.of(testCase);
        testExecutionRepository.save(testExecution);
        return runTest(testExecution);
    }

    public TestExecution runTest(TestExecution testExecution) throws
            TestDataValueNotFoundException, PreConditionViolationException, TestDriverException {
        try {
            if (environmentController != null)
                environmentController.setUp();
            testDriverContext = new TestDriverContext(this);
            var testInputs = testInputRepository.findByTestCase(testExecution.getTestCase());
            for (var testInput : testInputs) {
                var testExecutionInput = new TestExecutionInput();
                testExecutionInput.setTestExecution(testExecution);
                testExecutionInput.setTestInput(testInput);
                testExecutionInput.setStatus(TestExecutionInput.STATUS_INIT);
                testExecutionInputRepository.save(testExecutionInput);
            }
            for (var testInput : testInputs) {
                var testExecutionInput = testExecutionInputRepository
                        .findByTestInputAndTestExecution(testInput, testExecution).stream().findFirst()
                        .orElseThrow(IllegalStateException::new);
                testExecutionInput.setStatus(TestExecutionInput.STATUS_IN_USE);
                testExecutionInputRepository.save(testExecutionInput);
                var testDriverObservedOutputList = tryRunTestInput(testInput, testExecution);
                saveObservedOutputs(testDriverObservedOutputList, testExecution, testInput);
                testExecutionInput.setStatus(TestExecutionInput.STATUS_END);
                testExecutionInputRepository.save(testExecutionInput);
            }
            testExecution.setEndTime(new Date());
            testExecutionRepository.save(testExecution);
            return testExecution;
        } catch (EnvironmentException exc) {
            throw new TestDriverException(exc.getMessage(), exc);
        } finally {
            if (environmentController != null)
                environmentController.tearDown();
        }
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
