package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDriverMonitorResult {
    private boolean exitSuccess;
    private List<TestDriverObservedOutput> observedOutputs;
}
