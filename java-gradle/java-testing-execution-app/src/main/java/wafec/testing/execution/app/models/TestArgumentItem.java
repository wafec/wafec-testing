package wafec.testing.execution.app.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TestArgumentItem {
    private String name;
    private String value;
    @JsonProperty(value = "type")
    private String argType = "string";
}
