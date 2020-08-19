package wafec.testing.execution.app.commandline.models.virtualBox;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class GroupConfiguration {
    private Long id;
    private String name;
    @JsonProperty("program_cmd")
    private String programCmd;
    private List<MachineConfiguration> machines;
}
