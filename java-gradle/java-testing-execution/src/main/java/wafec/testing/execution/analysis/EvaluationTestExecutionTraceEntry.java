package wafec.testing.execution.analysis;

import lombok.Data;
import wafec.testing.execution.TestExecutionObservedOutput;

import javax.persistence.*;

@Entity
@Table(name = "TEST_EXECUTION_TRACE_ENTRY")
@Data
public class EvaluationTestExecutionTraceEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "evaluation_test_execution_trace_id", referencedColumnName = "id")
    private EvaluationTestExecutionTrace evaluationTestExecutionTrace;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_execution_observed_output_id", referencedColumnName = "id")
    private TestExecutionObservedOutput testExecutionObservedOutput;
    @Lob
    @Column(name = "discriminator", length = 56000)
    private String discriminator;
}
