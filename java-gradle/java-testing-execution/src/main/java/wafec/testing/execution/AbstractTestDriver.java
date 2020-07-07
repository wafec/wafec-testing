package wafec.testing.execution;

import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public abstract class AbstractTestDriver {
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
            TestDataValueNotFoundException, PreConditionViolationException {
        var testExecution = TestExecution.of(testCase);
        testExecutionRepository.save(testExecution);
        return runTest(testExecution);
    }

    public TestExecution runTest(TestExecution testExecution) throws TestDriverInputNotFoundException,
            TestDataValueNotFoundException, PreConditionViolationException {
        testDriverContext = new TestDriverContext(this);
        for (var testInput : testInputRepository.findByTestCase(testExecution.getTestCase())) {
            var testDriverObservedOutputList = tryRunTestInput(testInput, testExecution);
            int i = 0;
            for (var testDriverObservedOutput : testDriverObservedOutputList) {
                var testOutput = testOutputMapper.fromTestDriverObservedOutput(testDriverObservedOutput);
                testOutput.setCreatedAt(new Date());
                testOutput.setPosition(i++);
                TestExecutionObservedOutput observedOutput =
                        TestExecutionObservedOutput.of(testExecution, testInput, testOutput);
                testOutputRepository.save(testOutput);
                testExecutionObservedOutputRepository.save(observedOutput);
            }
        }
        testExecution.setEndTime(new Date());
        testExecutionRepository.save(testExecution);
        return testExecution;
    }

    private List<TestDriverObservedOutput> tryRunTestInput(TestInput testInput, TestExecution testExecution) throws
            TestDriverInputNotFoundException, TestDataValueNotFoundException, PreConditionViolationException {
        try {
            return runTestInput(testInput, testExecution);
        } catch (Exception exc) {
            var driverErrorOutput = TestDriverObservedOutput.error(exc.getClass().getName(), exc.getMessage());
            var testOutput = testOutputMapper.fromTestDriverObservedOutput(driverErrorOutput);
            testOutput.setCreatedAt(new Date());
            testOutput.setPosition(-1);
            TestExecutionObservedOutput observedOutput =
                    TestExecutionObservedOutput.of(testExecution, testInput, testOutput);
            testOutputRepository.save(testOutput);
            testExecutionObservedOutputRepository.save(observedOutput);
            throw exc;
        }
    }

    protected abstract List<TestDriverObservedOutput> runTestInput(TestInput testInput, TestExecution testExecution) throws TestDriverInputNotFoundException,
            TestDataValueNotFoundException, PreConditionViolationException;
    public abstract List<AcceptedInput> getAcceptedInputs();
}
