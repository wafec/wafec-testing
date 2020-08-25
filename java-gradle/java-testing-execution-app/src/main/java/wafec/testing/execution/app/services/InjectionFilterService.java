package wafec.testing.execution.app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wafec.testing.execution.robustness.InjectionTargetRepository;
import wafec.testing.execution.robustness.RobustnessTest;

@Transactional
@Component
public class InjectionFilterService {
    @Autowired
    private InjectionTargetRepository injectionTargetRepository;
    private Logger logger = LoggerFactory.getLogger(InjectionFilterService.class);

    public void applyFilters(RobustnessTest robustnessTest, InjectionFilters... filters) {
        if (filters != null && filters.length > 0) {
            logger.info("Applying filters");
            for (var filter : filters) {
                applyFilter(robustnessTest, filter);
            }
        }
    }

    private void applyFilter(RobustnessTest robustnessTest, InjectionFilters filter) {
        logger.info(String.format("Apply %s filter", filter));
        switch (filter) {
            case ONLY_DATA:
                applyOnlyDataFilter(robustnessTest);
                break;
        }
    }

    private void applyOnlyDataFilter(RobustnessTest robustnessTest) {
        injectionTargetRepository.deactivateNotTargetingDataInjections(robustnessTest);
        var count = injectionTargetRepository.countByNotTargetingDataInjections(robustnessTest);
        logger.info(String.format("ONLY_DATA discard: %d", count));
    }
}
