package wafec.testing.support.virtualbox.communication;

import lombok.Data;
import wafec.testing.support.virtualbox.VirtualBoxMachine;

import javax.persistence.*;

@Table(name = "VIRTUAL_BOX_MACHINE_PROCESS",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "virtual_box_machine_id", "name" })
    }
)
@Entity
@Data
public class VirtualBoxMachineProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "virtual_box_machine_id", referencedColumnName = "id")
    private VirtualBoxMachine virtualBoxMachine;
    @Column(name = "name")
    private String name;
    @Column(name = "process_type")
    private String processType;
}
