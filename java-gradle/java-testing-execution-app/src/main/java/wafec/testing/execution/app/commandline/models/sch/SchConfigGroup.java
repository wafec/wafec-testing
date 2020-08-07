package wafec.testing.execution.app.commandline.models.sch;

import lombok.Data;

import java.util.List;

@Data
public class SchConfigGroup {
    private Long id;
    private String host;
    private String username;
    private String password;
    private List<SchConfigCommand> commands;
}
