package wafec.testing.execution.robustness.analysis;

import lombok.Data;
import wafec.testing.execution.analysis.EvaluationTestExecution;
import wafec.testing.execution.robustness.RobustnessTestExecution;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ROBUSTNESS_EVALUATION_TEST_EXECUTION")
public class RobustnessEvaluationTestExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "evaluation_test_execution_id", referencedColumnName = "id")
    private EvaluationTestExecution evaluationTestExecution;
    @Column(name = "injection_succeed")
    private boolean injectionSucceed;
    @ManyToOne
    @JoinColumn(columnDefinition = "robustness_test_execution_id", referencedColumnName = "id")
    private RobustnessTestExecution robustnessTestExecution;
}
