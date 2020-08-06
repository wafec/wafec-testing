package wafec.testing.execution.robustness;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "INJECTION_TARGET_OPERATOR", indexes = {
        @Index(name = "IDX_ITO_INJECTION_KEY", columnList = "injection_key")
})
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
    @Column(name = "description", length = 4000)
    private String description;
    @Column(name = "field_value", length = 500000)
    private String fieldValue;
    @Column(name = "injection_value", length = 500000)
    private String injectionValue;
}
