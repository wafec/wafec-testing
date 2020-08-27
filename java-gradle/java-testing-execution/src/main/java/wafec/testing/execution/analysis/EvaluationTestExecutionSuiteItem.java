package wafec.testing.execution.analysis;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "EVALUATION_TEST_EXECUTION_SUITE_ITEM")
@Data
public class EvaluationTestExecutionSuiteItem {
    @EmbeddedId
    private EvaluationTestExecutionSuiteItemKey id;
    @ManyToOne
    @MapsId("evaluation_test_execution_suite_id")
    @JoinColumn(name = "evaluation_test_execution_suite_id")
    private EvaluationTestExecutionSuite evaluationTestExecutionSuite;
    @ManyToOne
    @MapsId("evaluation_test_execution_id")
    @JoinColumn(name = "evaluation_test_execution_id")
    private EvaluationTestExecution evaluationTestExecution;
}
