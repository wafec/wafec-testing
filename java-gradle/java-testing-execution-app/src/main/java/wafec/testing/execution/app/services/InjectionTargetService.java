package wafec.testing.execution.app.services;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wafec.testing.execution.robustness.InjectionTarget;
import wafec.testing.execution.robustness.InjectionTargetRepository;
import wafec.testing.execution.robustness.RobustnessTestRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class InjectionTargetService {
    @Autowired
    private InjectionTargetRepository injectionTargetRepository;
    @Autowired
    private RobustnessTestRepository robustnessTestRepository;

    Logger logger = LoggerFactory.getLogger(InjectionTargetService.class);

    public void discard(long robustnessTestId, boolean discard, boolean inverse, List<String> sourceKeys) {
        var robustnessTestOpt = robustnessTestRepository.findById(robustnessTestId);
        if (robustnessTestOpt.isPresent()) {
            var robustnessTest = robustnessTestOpt.get();
            for (var sourceKey : sourceKeys) {
                var injectionTargetList = injectionTargetRepository.findBySourceKeyAndRobustessTest(sourceKey, robustnessTest);
                if (inverse) {
                    var allList = injectionTargetRepository.findByRobustnessTest(robustnessTest);
                    var diffResult = CollectionUtils.subtract(allList.stream().map(InjectionTarget::getId).collect(Collectors.toList()),
                            injectionTargetList.stream().map(InjectionTarget::getId).collect(Collectors.toList()));
                    injectionTargetList = diffResult.stream().map(id -> allList.stream().filter(t -> t.getId() == id).findFirst().orElseThrow())
                            .collect(Collectors.toList());
                }
                logger.info(String.format("BEGIN SourceKey '%s', Discard %s, Size: %d", sourceKey, discard, injectionTargetList.size()));
                for (var injectionTarget : injectionTargetList) {
                    injectionTarget.setDiscard(discard);
                    injectionTargetRepository.save(injectionTarget);
                    logger.debug(String.format("InjectionTarget: %s, Discard: %s", injectionTarget.getQualifiedDescription(), discard));
                }
                logger.info(String.format("  END SourceKey '%s', Discard %s", sourceKey, discard));
            }
        } else {
            throw new IllegalArgumentException("RobustnessTest not found");
        }
    }
}
