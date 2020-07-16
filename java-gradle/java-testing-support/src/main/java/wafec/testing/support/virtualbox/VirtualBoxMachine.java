package wafec.testing.support.virtualbox;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "VIRTUAL_BOX_MACHINE")
@Data
@ToString
public class VirtualBoxMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    private String name;
    private String snapshot;
    @Column(name = "in_use")
    private boolean inUse;
    @ManyToOne
    @JoinColumn(columnDefinition = "virtual_machine_group_id", referencedColumnName = "id")
    private VirtualBoxMachineGroup virtualBoxMachineGroup;
}
