package wafec.testing.execution.analysis;

import lombok.Data;
import wafec.testing.execution.TestExecution;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EVALUATION_TEST_EXECUTION")
@Data
public class EvaluationTestExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_execution_id", referencedColumnName = "id")
    private TestExecution testExecution;
    @Column(name ="pass_succeed")
    private boolean passSucceed;
    @Column(name = "with_text_error_present")
    private boolean withTextErrorPresent;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(columnDefinition = "evaluation_test_execution_status_id", referencedColumnName = "id")
    private EvaluationTestExecutionStatus evaluationTestExecutionStatus;
    @Column(name = "generated_at")
    private Date generatedAt;
}
