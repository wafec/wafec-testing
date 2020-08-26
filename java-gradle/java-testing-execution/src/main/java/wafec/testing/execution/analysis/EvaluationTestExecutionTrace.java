package wafec.testing.execution.analysis;

import lombok.Data;
import wafec.testing.execution.TestExecution;

import javax.persistence.*;

@Entity
@Table(name = "EVALUATION_TEST_EXECUTION_TRACE")
@Data
public class EvaluationTestExecutionTrace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @Column(name = "trace_type")
    @Enumerated(EnumType.STRING)
    private EvaluationTestExecutionTraceTypes traceType;
    @ManyToOne
    @JoinColumn(columnDefinition = "evaluation_test_execution_id", referencedColumnName = "id")
    private EvaluationTestExecution evaluationTestExecution;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_execution_source_id", referencedColumnName = "id")
    private TestExecution testExecutionSource;
    @Enumerated(EnumType.STRING)
    @Column(name = "test_execution_source_type")
    private TestExecutionSourceTypes testExecutionSourceType;
}
