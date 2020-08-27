package wafec.testing.execution.robustness.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import wafec.testing.execution.TestExecution;
import wafec.testing.execution.analysis.AbstractTestExecutionEvaluator;
import wafec.testing.execution.analysis.EvaluationTestExecution;
import wafec.testing.execution.robustness.InjectionFaultRepository;
import wafec.testing.execution.robustness.RobustnessTestExecution;
import wafec.testing.execution.robustness.RobustnessTestExecutionRepository;

import java.util.List;

public abstract class AbstractRobustnessTestExecutionEvaluator extends AbstractTestExecutionEvaluator {
    @Autowired
    private RobustnessEvaluationTestExecutionRepository robustnessEvaluationTestExecutionRepository;
    @Autowired
    private InjectionFaultRepository injectionFaultRepository;
    @Autowired
    private RobustnessTestExecutionRepository robustnessTestExecutionRepository;

    @Override
    public EvaluationTestExecution analyze(TestExecution testExecution) {
        RobustnessTestExecution robustnessTestExecution = robustnessTestExecutionRepository
                .findByTestExecution(testExecution);
        return analyze(robustnessTestExecution)
                .getEvaluationTestExecution();
    }

    public RobustnessEvaluationTestExecution analyze(RobustnessTestExecution robustnessTestExecution) {
        RobustnessEvaluationTestExecution robustnessEvaluationTestExecution = new RobustnessEvaluationTestExecution();
        robustnessEvaluationTestExecution.setRobustnessTestExecution(robustnessTestExecution);
        robustnessEvaluationTestExecutionRepository.save(robustnessEvaluationTestExecution);
        EvaluationTestExecution evaluationTestExecution = super.analyze(robustnessTestExecution.getTestExecution());
        robustnessEvaluationTestExecution.setEvaluationTestExecution(evaluationTestExecution);
        robustnessEvaluationTestExecution.setInjectionSucceed(hasInjectionSucceed(robustnessEvaluationTestExecution));
        robustnessEvaluationTestExecutionRepository.save(robustnessEvaluationTestExecution);
        return robustnessEvaluationTestExecution;
    }

    protected boolean hasInjectionSucceed(RobustnessEvaluationTestExecution robustnessEvaluationTestExecution) {
        var injectionFaultList = injectionFaultRepository.findByRobustnessTestExecution(robustnessEvaluationTestExecution.getRobustnessTestExecution());
        return injectionFaultList.stream().anyMatch(i -> i.isUsed() && i.getInjectionTargetOperator() != null);
    }
}
