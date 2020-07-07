package wafec.testing.execution;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TEST_INPUT_EXPECTED_OUTPUT")
public class TestInputExpectedOutput {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_input_id", referencedColumnName = "id")
    private TestInput testInput;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_output_id", referencedColumnName = "id")
    private TestOutput testOutput;
}
