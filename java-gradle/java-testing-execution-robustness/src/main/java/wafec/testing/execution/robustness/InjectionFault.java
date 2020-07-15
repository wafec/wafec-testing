package wafec.testing.execution.robustness;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "INJECTION_FAULT")
@Data
public class InjectionFault {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "robustness_test_execution_id", referencedColumnName = "id")
    private RobustnessTestExecution robustnessTestExecution;
    @Column(name = "source_key")
    private String sourceKey;
    private String context;
    private boolean used;
}
