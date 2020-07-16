package wafec.testing.execution.robustness;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "INJECTION_TARGET_OPERATOR")
public class InjectionTargetOperator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne()
    @JoinColumn(columnDefinition = "injection_target_id", referencedColumnName = "id")
    private InjectionTarget injectionTarget;
    @Column(name = "injection_key")
    private String injectionKey;
    private boolean used;
    private boolean ignored;
    private String description;
    @Column(name = "field_value")
    private String fieldValue;
    @Column(name = "injection_value")
    private String injectionValue;
}
