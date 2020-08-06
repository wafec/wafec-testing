package wafec.testing.execution.robustness;

import lombok.Data;

import javax.persistence.*;

@Table(name = "INJECTION_TARGET_MANAGED", indexes = {
        @Index(name = "IDX_ITM_INJECTOR_NAME", columnList = "injector_name")
})
@Entity
@Data
public class InjectionTargetManaged {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "injection_target_id", referencedColumnName = "id")
    private InjectionTarget injectionTarget;
    @Column(name = "injector_name")
    private String injectorName;
    @Column(name = "in_use")
    private boolean inUse;
}
