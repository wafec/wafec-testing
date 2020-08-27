package wafec.testing.execution.analysis;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "EVALUATION_TEST_EXECUTION_SUITE_RESULT_TEST_EXECUTION")
@Data
public class EvaluationTestExecutionSuiteResultTestExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "evaluation_test_execution_suite_result_id", referencedColumnName = "id")
    private EvaluationTestExecutionSuiteResult evaluationTestExecutionSuiteResult;
    @ManyToOne
    @JoinColumn(columnDefinition = "evaluation_test_execution_id", referencedColumnName = "id")
    private EvaluationTestExecution evaluationTestExecution;
}
