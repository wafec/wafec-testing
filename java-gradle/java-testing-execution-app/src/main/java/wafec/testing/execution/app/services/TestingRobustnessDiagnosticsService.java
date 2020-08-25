package wafec.testing.execution.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.execution.robustness.RobustnessTestExecutionRepository;
import wafec.testing.execution.robustness.RobustnessTestRepository;

@Component
public class TestingRobustnessDiagnosticsService {
    @Autowired
    private RobustnessTestRepository robustnessTestRepository;
    @Autowired
    private RobustnessTestExecutionRepository robustnessTestExecutionRepository;

    public void buildReport(long robustnessTestId) {
        var robustnessTest = robustnessTestRepository.findById(robustnessTestId).orElseThrow();
        var robustnessTestExecutionList = robustnessTestExecutionRepository.findByRobustnessTest(robustnessTest);
        for (var robustnessTestExecution : robustnessTestExecutionList) {

        }
    }
}
