package wafec.testing.execution.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wafec.testing.execution.*;

@Service
@Transactional
public class TestClearService {
    @Autowired
    private TestCaseRepository testCaseRepository;
    @Autowired
    private TestExecutionObservedOutputRepository testExecutionObservedOutputRepository;
    @Autowired
    private TestOutputRepository testOutputRepository;
    @Autowired
    TestExecutionRepository testExecutionRepository;

    public void clearTest(long testCaseId) {
        TestCase testCase = testCaseRepository.findById(testCaseId).orElseThrow(IllegalArgumentException::new);
        clearTest(testCase);
    }

    public void clearTest(TestCase testCase) {
        var outputs = testOutputRepository.findByTestCase(testCase);
        var observedOutputs = testExecutionObservedOutputRepository.findByTestCase(testCase);
        var testExecutions = testExecutionRepository.findByTestCase(testCase);
        for (var testExecution : testExecutions) {
            testExecutionRepository.delete(testExecution);
        }
        for (var observedOutput : observedOutputs) {
            testExecutionObservedOutputRepository.delete(observedOutput);
        }
        for(var output : outputs) {
            testOutputRepository.delete(output);
        }
    }
}
