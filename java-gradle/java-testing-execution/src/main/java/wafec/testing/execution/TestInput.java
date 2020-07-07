package wafec.testing.execution;

import lombok.Data;

import javax.persistence.*;
import java.util.Optional;

@Data
@Entity
@Table(name = "TEST_INPUT")
public class TestInput implements InputSignature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    private int position;
    @ManyToOne
    @JoinColumn(name = "test_case_id", referencedColumnName = "id")
    private TestCase testCase;
    private String name;
    private String signature;

    @Override
    public boolean match(InputSignature otherSignature) {
        return Optional.ofNullable(otherSignature).map(InputSignature::getSignature)
                .equals(Optional.ofNullable(signature));
    }
}
