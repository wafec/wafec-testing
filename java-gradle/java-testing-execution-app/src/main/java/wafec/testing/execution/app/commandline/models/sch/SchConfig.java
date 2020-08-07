package wafec.testing.execution.app.commandline.models.sch;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName(value = "configuration")
public class SchConfig {
    private List<SchConfigGroup> groups;
}
