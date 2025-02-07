package wafec.testing.execution.app.commandline;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import picocli.CommandLine.*;
import wafec.testing.execution.app.services.InjectionTargetService;
import wafec.testing.execution.robustness.InjectionTarget;
import wafec.testing.execution.robustness.InjectionTargetRepository;
import wafec.testing.execution.robustness.RobustnessTest;
import wafec.testing.execution.robustness.RobustnessTestRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Command(name = "discard")
public class TestingRobustnessInjectionTargetDiscard implements Callable<Integer> {
    @Option(names = { "-i", "--inverse" }, defaultValue = "false")
    private boolean inverse;
    @Option(names = { "-r", "--robustness-test-id" }, required = true)
    private long robustnessTestId;
    @Option(names = { "-n", "--negative" }, required = false)
    private boolean negative;
    @Parameters(paramLabel = "SOURCE-KEY", index = "0..*")
    private List<String> sourceKeys;

    @Autowired
    private InjectionTargetService injectionTargetService;

    Logger logger = LoggerFactory.getLogger(TestingRobustnessInjectionTargetDiscard.class);

    @Override
    public Integer call() throws Exception {
        injectionTargetService.discard(robustnessTestId, !negative, inverse, sourceKeys);
        return 0;
    }
}
