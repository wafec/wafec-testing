package wafec.testing.execution.utils;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "SCH_OUTPUT_COMMAND")
public class SchOutputCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @ManyToOne
    @JoinColumn(columnDefinition = "group_id", referencedColumnName = "id")
    private SchOutputCommandGroup group;
    private String command;
    private int priority;
    private String source;
    private boolean suppress;
}
