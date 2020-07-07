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

    public TestExecution runTest(TestCase testCase) throws TestDriverInputNotFoundException,
            TestDataValueNotFoundException, PreConditionViolationException {
        var testExecution = TestExecution.of(testCase);
        return runTest(testExecution);
    }

    @Transactional
    public TestExecution runTest(TestExecution testExecution) throws TestDriverInputNotFoundException,
            TestDataValueNotFoundException, PreConditionViolationException {
        for (var testInput : testInputRepository.findByTestCase(testExecution.getTestCase())) {
            var testDriverObservedOutputList = runTestInput(testInput, testExecution);
            int i = 0;
            for (var testDriverObservedOutput : testDriverObservedOutputList) {
                var testOutput = testOutputMapper.fromTestDriverObservedOutput(testDriverObservedOutput);
                testOutput.setCreatedAt(new Date());
                testOutput.setPosition(i++);
                TestExecutionObservedOutput observedOutput =
                        TestExecutionObservedOutput.of(testExecution, testInput, testOutput);
                testExecutionObservedOutputRepository.save(observedOutput);
            }
        }
        testExecution.setEndTime(new Date());
        testExecutionRepository.save(testExecution);
        return testExecution;
    }

    protected abstract List<TestDriverObservedOutput> runTestInput(TestInput testInput, TestExecution testExecution) throws TestDriverInputNotFoundException,
            TestDataValueNotFoundException, PreConditionViolationException;
    public abstract List<AcceptedInput> getAcceptedInputs();
}
