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
    @ManyToMany
    @JoinTable(
            name = "SCH_OUTPUT_COMMAND_SET_GROUP",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<SchOutputCommandGroup> schOutputCommandGroups;
}
