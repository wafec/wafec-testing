package wafec.testing.execution.app.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseItem {
    @JsonProperty("target-system")
    private String targetSystem;
    private String description;
    @JsonProperty("test-inputs")
    private List<TestInputItem> testInputs;
}