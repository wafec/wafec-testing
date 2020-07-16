package wafec.testing.execution.app.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestInputItem {
    private String name;
    private String signature;
    private Boolean important;
    @JsonProperty("test-args")
    private List<TestArgumentItem> testArguments;
}
