package wafec.testing.execution.analysis;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(
        name = "EVALUATION_TEST_EXECUTION_SUITE_RESULT",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "result", "evaluation_test_execution_suite_id"
                })
        }
)
@Data
public class EvaluationTestExecutionSuiteResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @Lob
    @Column(length = 56000)
    private String result;
    @Column(name = "occurrence_count")
    private long occurrenceCount;
    @ManyToOne
    @JoinColumn(columnDefinition = "evaluation_test_execution_suite_id", referencedColumnName = "id")
    private EvaluationTestExecutionSuite evaluationTestExecutionSuite;
}
