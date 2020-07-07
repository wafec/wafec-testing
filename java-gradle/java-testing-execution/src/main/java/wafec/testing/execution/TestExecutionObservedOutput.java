package wafec.testing.execution;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TEST_EXECUTION_OBSERVED_OUTPUT")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class TestExecutionObservedOutput {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_execution_id", referencedColumnName = "id")
    @NonNull private TestExecution testExecution;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_input_id", referencedColumnName = "id")
    @NonNull private TestInput testInput;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_output_id", referencedColumnName = "id")
    @NonNull private TestOutput testOutput;
}
