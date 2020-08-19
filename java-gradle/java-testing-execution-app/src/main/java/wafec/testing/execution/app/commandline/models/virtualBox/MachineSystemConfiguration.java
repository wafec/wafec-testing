package wafec.testing.execution.app.commandline.models.virtualBox;

import lombok.Data;

@Data
public class MachineSystemConfiguration {
    private Long id;
    private String username;
    private String password;
    private String address;
}
