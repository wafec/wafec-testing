package wafec.testing.execution.robustness;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "INJECTION_TARGET")
@Data
public class InjectionTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @Column(name = "source_key")
    private String sourceKey;
    private String context;
    @ManyToOne
    @JoinColumn(columnDefinition = "robustness_test_id", referencedColumnName = "id")
    private RobustnessTest robustnessTest;
    private long monitorCount;
    private boolean discard;
    @Column(length = 15000)
    private String description;

    public String getQualifiedDescription() {
        return String.format("%s.%s", sourceKey, context);
    }

    public String getInjectionTargetUniqueKey() {
        return String.format("%s %s", sourceKey, context);
    }
}
