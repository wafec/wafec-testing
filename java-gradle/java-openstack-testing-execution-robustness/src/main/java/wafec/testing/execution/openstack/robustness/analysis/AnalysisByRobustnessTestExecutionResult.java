package wafec.testing.execution.openstack.robustness.analysis;

import lombok.Data;
import lombok.ToString;
import wafec.testing.execution.robustness.InjectionTargetOperator;
import wafec.testing.execution.robustness.RobustnessTestExecution;

import java.util.List;

@Data
@ToString
public class AnalysisByRobustnessTestExecutionResult {
    @ToString.Exclude
    private RobustnessTestExecution robustnessTestExecution;
    private boolean injectionSucceed;
    private boolean errorInSchLogs;
    private boolean succeed;
    @ToString.Exclude
    private List<String> executionTrace;
    @ToString.Exclude
    private List<String> stateTrace;
    private OpenStackStateDestinationTypes status;
    private String statusDescription;
    @ToString.Exclude
    private RobustnessTestExecution robustnessTestExecutionRef;
    @ToString.Exclude
    private List<String> executionTraceRef;
    @ToString.Exclude
    private List<String> stateTraceRef;
}
