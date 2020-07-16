package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TEST_EXECUTION_INPUT")
@Data
public class TestExecutionInput {
    public static final String STATUS_INIT = "init";
    public static final String STATUS_END = "end";
    public static final String STATUS_IN_USE = "in-use";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_input_id", referencedColumnName = "id")
    private TestInput testInput;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_execution_id", referencedColumnName = "id")
    private TestExecution testExecution;
    private String status;
}
