package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TEST_INPUT_PARAMETER_DATA")
public class TestInputParameterData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne()
    @JoinColumn(columnDefinition = "test_input_parameter_id", referencedColumnName = "id")
    private TestInputParameter testInputParameter;
    @Lob
    @Column(name = "data", length = 500000)
    private String data;
}
