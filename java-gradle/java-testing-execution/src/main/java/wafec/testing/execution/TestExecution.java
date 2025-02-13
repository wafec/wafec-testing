package wafec.testing.execution;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "TEST_EXECUTION")
public class TestExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @ManyToOne
    @JoinColumn(name = "test_case_id", referencedColumnName = "id")
    private TestCase testCase;
    @Transient
    private TestExecutionInput currentTestExecutionInput;
    @Transient
    private EnvironmentController environmentController;

    public static TestExecution of(TestCase testCase) {
        TestExecution testExecution = new TestExecution();
        testExecution.startTime = new Date();
        testExecution.testCase = testCase;
        return testExecution;
    }
}
