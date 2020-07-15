package wafec.testing.execution.robustness;

import lombok.Data;
import wafec.testing.execution.TestCase;

import javax.persistence.*;

@Entity
@Table(name = "ROBUSTNESS_TEST")
@Data
public class RobustnessTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_case_id", referencedColumnName = "id")
    private TestCase testCase;
}
