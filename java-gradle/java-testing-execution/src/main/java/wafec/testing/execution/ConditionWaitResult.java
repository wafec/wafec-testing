package wafec.testing.execution;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class ConditionWaitResult {
    private long elapsedTime;
    private boolean exitSuccess;
    private List<Exception> errors = new ArrayList<>();
}
