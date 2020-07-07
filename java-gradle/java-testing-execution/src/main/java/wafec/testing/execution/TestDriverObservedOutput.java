package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TestDriverObservedOutput {
    private String output;
    private String source;
    private String sourceType;

    public static TestDriverObservedOutput none() {
        return TestDriverObservedOutput.of("none", "none", "none");
    }

    public List<TestDriverObservedOutput> asList() {
        var list = new ArrayList<TestDriverObservedOutput>();
        list.add(this);
        return list;
    }

    public static TestDriverObservedOutput success(String source) {
        return TestDriverObservedOutput.of("success", source, source);
    }
}
