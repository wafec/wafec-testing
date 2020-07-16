package wafec.testing.execution.app.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestArgumentItem {
    private String name;
    private String value;
    @JsonProperty(value = "type")
    private String argType;
}
