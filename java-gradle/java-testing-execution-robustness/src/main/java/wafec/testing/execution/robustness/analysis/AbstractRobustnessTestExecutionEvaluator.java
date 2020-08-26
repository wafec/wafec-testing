package wafec.testing.execution.robustness.analysis;

import org.springframework.beans.factory.annotation.Autowired;
import wafec.testing.execution.analysis.AbstractTestExecutionEvaluator;
import wafec.testing.execution.analysis.EvaluationTestExecution;
import wafec.testing.execution.robustness.InjectionFaultRepository;
import wafec.testing.execution.robustness.RobustnessTestExecution;

import java.util.List;

public abstract class AbstractRobustnessTestExecutionEvaluator extends AbstractTestExecutionEvaluator {
    @Autowired
    private RobustnessEvaluationTestExecutionRepository robustnessEvaluationTestExecutionRepository;
    @Autowired
    private InjectionFaultRepository injectionFaultRepository;

    public RobustnessEvaluationTestExecution analyze(RobustnessTestExecution robustnessTestExecution) {
        RobustnessEvaluationTestExecution robustnessEvaluationTestExecution = new RobustnessEvaluationTestExecution();
        EvaluationTestExecution evaluationTestExecution = analyze(robustnessTestExecution.getTestExecution());
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
