package wafec.testing.execution.app.commandline.models.virtualBox;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MachineProcessConfiguration {
    private Long id;
    private String name;
    @JsonProperty("type")
    private String processType;
}
