package wafec.testing.execution.app.commandline;

import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import wafec.testing.execution.analysis.EvaluationTestExecutionSuite;
import wafec.testing.execution.analysis.EvaluationTestExecutionSuiteRepository;
import wafec.testing.execution.openstack.robustness.analysis.OpenStackRobustnessEvaluationPresenter;
import wafec.testing.execution.openstack.robustness.analysis.OpenStackRobustnessTestExecutionEvaluator;
import wafec.testing.execution.robustness.RobustnessTestExecutionRepository;
import wafec.testing.execution.robustness.RobustnessTestRepository;
import wafec.testing.execution.robustness.analysis.RobustnessEvaluationTestExecution;
import wafec.testing.execution.robustness.analysis.RobustnessEvaluationTestExecutionRepository;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@CommandLine.Command(name = "analyze")
public class TestingRobustnessOpenStackAnalyze implements Callable<Integer> {
    @CommandLine.ArgGroup(exclusive = true)
    Composite composite;
    @CommandLine.Option(names = { "-d", "--data" })
    boolean data;

    static class Composite {
        @CommandLine.ArgGroup(exclusive = false)
        ByTestExecution byTestExecution;
        @CommandLine.ArgGroup(exclusive = false)
        ByTest byTest;
    }

    static class ByTestExecution {
        @CommandLine.Option(names = { "-e", "--robustness-test-execution" }) long id;
    }

    static class ByTest {
        @CommandLine.Option(names = { "-r", "--robustness-test" }) long id;
    }

    @Autowired
    RobustnessTestExecutionRepository robustnessTestExecutionRepository;
    @Autowired
    OpenStackRobustnessTestExecutionEvaluator robustnessTestExecutionEvaluator;
    @Autowired
    OpenStackRobustnessEvaluationPresenter openStackRobustnessEvaluationPresenter;
    @Autowired
    RobustnessEvaluationTestExecutionRepository robustnessEvaluationTestExecutionRepository;
    @Autowired
    RobustnessTestRepository robustnessTestRepository;
    @Autowired
    EvaluationTestExecutionSuiteRepository evaluationTestExecutionSuiteRepository;

    @Override
    public Integer call() throws Exception {
        if (composite.byTestExecution != null) {
            System.out.println(String.format("Evaluation by Test Execution and Data is %b", data));
            RobustnessEvaluationTestExecution evaluation;
            if (data) {
                evaluation = robustnessEvaluationTestExecutionRepository.findById(composite.byTestExecution.id).orElseThrow();
            } else {
                var robustnessTestExecution = robustnessTestExecutionRepository.findById(composite.byTestExecution.id).orElseThrow();
                evaluation = robustnessTestExecutionEvaluator.analyze(robustnessTestExecution);
            }
            openStackRobustnessEvaluationPresenter.printResultsFor(evaluation);
        } else if (composite.byTest != null) {
            System.out.println(String.format("Evaluation by Test and Data is %b", data));
            EvaluationTestExecutionSuite suite;
            if (data) {
                suite = evaluationTestExecutionSuiteRepository.findById(composite.byTest.id).orElseThrow();
            } else {
                var robustnessTest = robustnessTestRepository.findById(composite.byTest.id).orElseThrow();
                var robustnessTestExecutionList = robustnessTestExecutionRepository.findByRobustnessTest(robustnessTest);
                var evaluationList = new ArrayList<RobustnessEvaluationTestExecution>();
                int count = 1;
                for (var robustnessTestExecution : robustnessTestExecutionList) {
                    var evaluation = robustnessTestExecutionEvaluator.analyze(robustnessTestExecution);
                    evaluationList.add(evaluation);
                    System.out.println(String.format("  Evaluation %d did return [%d/%d]", evaluation.getId(),
                            count++, evaluationList.size()));
                }
                suite = robustnessTestExecutionEvaluator.analyzeAndGetSuite(evaluationList
                        .stream()
                        .map(RobustnessEvaluationTestExecution::getEvaluationTestExecution)
                        .collect(Collectors.toList())
                );
            }
            openStackRobustnessEvaluationPresenter.printResultsFor(suite);
        }
        return 0;
    }
}
