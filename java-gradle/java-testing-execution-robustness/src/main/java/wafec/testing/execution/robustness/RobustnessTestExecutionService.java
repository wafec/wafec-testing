package wafec.testing.execution.robustness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class RobustnessTestExecutionService {
    @Autowired
    private RobustnessTestExecutionRepository robustnessTestExecutionRepository;

    public RobustnessTestExecution findRobustnessTestExecutionReference(RobustnessTestExecution robustnessTestExecution) {
        return findRobustnessTestExecutionReference(robustnessTestExecution.getRobustnessTest());
    }

    public RobustnessTestExecution findRobustnessTestExecutionReference(RobustnessTest robustnessTest) {
        var robustnessTestExecutionRefList = robustnessTestExecutionRepository.findByRobustnessTestAndScanIsTrue(robustnessTest);
        if (robustnessTestExecutionRefList != null && robustnessTestExecutionRefList.size() > 0) {
            robustnessTestExecutionRefList.sort(Comparator.comparing(m -> m.getTestExecution().getEndTime()));
            return robustnessTestExecutionRefList.get(robustnessTestExecutionRefList.size() - 1);
        } else {
            return null;
        }
    }
}
