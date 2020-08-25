package wafec.testing.support.virtualbox.communication;

import lombok.Data;
import wafec.testing.support.virtualbox.VirtualBoxMachine;

import javax.persistence.*;

@Table(name = "VIRTUAL_BOX_MACHINE_LINUX",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "virtual_box_machine_id", "address" })
    }
)
@Entity
@Data
public class VirtualBoxMachineLinux {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    private String username;
    private String passwd;
    private String address;
    @ManyToOne
    @JoinColumn(columnDefinition = "virtual_box_machine_id", referencedColumnName = "id")
    private VirtualBoxMachine virtualBoxMachine;
}
