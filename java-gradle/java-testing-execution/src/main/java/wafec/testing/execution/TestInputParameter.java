package wafec.testing.execution;

import lombok.*;

import javax.persistence.*;

@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "TEST_INPUT_PARAMETER")
public class TestInputParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne()
    @JoinColumn(columnDefinition = "test_input_id", referencedColumnName = "id")
    @NonNull private TestInput testInput;
    @NonNull private String name;
    @Column(name = "param_type")
    @NonNull private String paramType;
}
