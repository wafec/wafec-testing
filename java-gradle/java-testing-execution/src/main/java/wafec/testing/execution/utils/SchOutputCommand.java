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
    @Column(name = "date_pattern")
    private String datePattern;
    @Column(name = "ignore_on_error")
    private boolean ignoreOnError;
    @Column(name = "ignore_if_invalid")
    private boolean ignoreIfInvalid;
}
