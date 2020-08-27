package wafec.testing.execution.analysis;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class EvaluationTestExecutionSuiteItemKey implements Serializable {
    @Column(name = "evaluation_test_execution_suite_id")
    private long evaluationTestExecutionSuiteId;
    @Column(name = "evaluation_test_execution_id")
    private long evaluationTestExecutionId;
}
