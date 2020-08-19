package wafec.testing.execution.utils;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table(name = "SCH_OUTPUT_COMMAND_SET")
@Entity
@Data
public class SchOutputCommandSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "SCH_OUTPUT_COMMAND_SET_GROUP",
            joinColumns = @JoinColumn(name = "sch_output_command_set_id"),
            inverseJoinColumns = @JoinColumn(name = "sch_output_command_group_id")
    )
    private List<SchOutputCommandGroup> schOutputCommandGroups;
}
