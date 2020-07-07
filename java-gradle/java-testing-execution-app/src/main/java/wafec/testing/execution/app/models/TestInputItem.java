package wafec.testing.execution.app.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TestInputItem {
    private String name;
    private String signature;
    @JsonProperty("test-args")
    private List<TestArgumentItem> testArguments;
}
