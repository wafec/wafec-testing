package wafec.testing.execution.app.commandline.models.sch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SchConfigCommand {
    private Long id;
    private String command;
    private Integer priority;
    private Boolean suppress;
    @JsonProperty("ignore-on-error")
    private Boolean ignoreOnError;
    @JsonProperty("ignore-if-invalid")
    private Boolean ignoreIfInvalid;
    private String source;
    @JsonProperty("date-pattern")
    private String datePattern;
}
