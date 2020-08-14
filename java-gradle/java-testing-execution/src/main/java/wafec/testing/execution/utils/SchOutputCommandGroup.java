package wafec.testing.execution.utils;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "SCH_OUTPUT_COMMAND_GROUP")
public class SchOutputCommandGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    private String host;
    private String username;
    private String passwd;
    @ManyToMany(mappedBy = "schOutputCommandGroups")
    private List<SchOutputCommandSet> schOutputCommandSets;
}
