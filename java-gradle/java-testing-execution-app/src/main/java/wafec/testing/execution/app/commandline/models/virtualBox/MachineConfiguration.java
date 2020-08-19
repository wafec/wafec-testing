package wafec.testing.execution.app.commandline.models.virtualBox;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class MachineConfiguration {
    private Long id;
    private String name;
    private String snapshot;
    @JsonProperty("operating_system")
    private String operatingSystem;
    private List<MachineProcessConfiguration> processes;
    private List<MachineSystemConfiguration> systems;
}
