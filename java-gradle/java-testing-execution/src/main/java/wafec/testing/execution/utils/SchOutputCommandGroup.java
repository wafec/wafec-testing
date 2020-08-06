package wafec.testing.execution.utils;

import lombok.Data;

import javax.persistence.*;

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
}
