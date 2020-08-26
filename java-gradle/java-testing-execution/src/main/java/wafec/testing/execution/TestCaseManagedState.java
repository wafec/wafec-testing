package wafec.testing.execution;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "TEST_CASE_MANAGED_STATE")
@Data
public class TestCaseManagedState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "test_case_id", referencedColumnName = "id")
    private TestCase testCase;
    private String name;
}
