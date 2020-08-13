package wafec.testing.execution.app.commandline;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import wafec.testing.execution.robustness.InjectionTargetManagedRepository;
import wafec.testing.execution.robustness.InjectionTargetRepository;
import wafec.testing.execution.robustness.RobustnessTestExecutionRepository;
import wafec.testing.execution.robustness.RobustnessTestRepository;

import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@CommandLine.Command(name = "summary")
public class TestingRobustnessInjectionSummary implements Callable<Integer> {
    @CommandLine.Option(names = { "-r", "--robustness-test-id" }, required = true)
    long robustnessTestId;

    @Autowired
    RobustnessTestRepository robustnessTestRepository;
    @Autowired
    InjectionTargetManagedRepository managedRepository;
    @Autowired
    InjectionTargetRepository targetRepository;
    @Autowired
    RobustnessTestExecutionRepository robustnessTestExecutionRepository;

    @Override
    public Integer call() throws Exception {
        var robustnessTest = robustnessTestRepository.findById(robustnessTestId).orElseThrow();
        var discardTrueList = targetRepository.findSourceKeysByRobustnessTestAndDiscardIsTrue(robustnessTest);
        var discardFalseList = targetRepository.findSourceKeysByRobustnessTestAndDiscardIsFalse(robustnessTest);
        var countIsZero = managedRepository.countByRobustnessTestAndInjectionCountEqualToZero(robustnessTest);
        var countIsNotZero = managedRepository.countByRobustnessTestAndInjectionCountGreaterThanZero(robustnessTest);
        var testExecutionList = robustnessTestExecutionRepository.findTestExecutionByRobustnessTest(robustnessTest);
        var averageTestExecution = testExecutionList.stream()
                .filter(te -> te.getStartTime() != null && te.getEndTime() != null)
                .collect(Collectors.averagingLong(te -> te.getEndTime().getTime() - te.getStartTime().getTime()));

        System.out.println("#### SUMMARY BEGIN");
        System.out.println("##");
        System.out.println(String.format("## Will Test:  %d", countIsZero));
        System.out.println(String.format("## Was Tested: %d", countIsNotZero));

        System.out.println(String.format("## Average Time: %s", DurationFormatUtils.formatDuration(averageTestExecution.longValue(), "HH:mm:ss:SS")));
        System.out.println(String.format("## Estimated Time: %s", DurationFormatUtils.formatDuration(averageTestExecution.longValue() * countIsZero, "yyyy-MM-dd HH:mm:ss:SS")));
        if (discardFalseList.size() > 0) {
            System.out.println("## NOT DISCARD:");
            for (var sourceKey : discardFalseList) {
                System.out.println(String.format("## + %s (%d)", sourceKey.getSourceKey(), sourceKey.getSourceKeyCount()));
            }
        }
        if (discardTrueList.size() > 0) {
            System.out.println("## DISCARD:");
            for (var sourceKey : discardTrueList) {
                System.out.println(String.format("## - %s (%d)", sourceKey.getSourceKey(), sourceKey.getSourceKeyCount()));
            }
        }
        System.out.println("##");
        System.out.println("#### SUMMARY END");

        return 0;
    }
}
