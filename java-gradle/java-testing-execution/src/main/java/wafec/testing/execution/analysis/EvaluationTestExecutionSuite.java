package wafec.testing.execution.analysis;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EVALUATION_TEST_EXECUTION_SUITE")
@Data
public class EvaluationTestExecutionSuite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToMany
    @JoinTable(
            name = "EVALUATION_TEST_EXECUTION_SUITE_ITEM",
            joinColumns = @JoinColumn(name = "evaluation_test_execution_suite_id"),
            inverseJoinColumns = @JoinColumn(name = "evaluation_test_execution_id")
    )
    private List<EvaluationTestExecution> evaluationTestExecutionList;
    @Column(name = "generated_at")
    private Date generatedAt;
}
