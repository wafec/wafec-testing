package wafec.testing.support.virtualbox;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "VIRTUAL_BOX_MACHINE_GROUP")
@Data
@ToString
public class VirtualBoxMachineGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "exec_cmd")
    private String programCmd;
}
