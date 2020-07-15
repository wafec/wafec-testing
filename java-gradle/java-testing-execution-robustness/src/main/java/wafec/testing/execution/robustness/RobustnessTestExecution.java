package wafec.testing.execution.robustness;


import lombok.Data;
import wafec.testing.execution.TestExecution;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ROBUSTNESS_TEST_EXECUTION")
public class RobustnessTestExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "robustness_test_id", referencedColumnName = "id")
    private RobustnessTest robustnessTest;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_execution_id", referencedColumnName = "id")
    private TestExecution testExecution;
}
