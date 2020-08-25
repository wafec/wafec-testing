package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TestDriverObservedOutput {
    private String output;
    private String source;
    private String sourceType;
    private Date createdAt = new Date();

    public static TestDriverObservedOutput of(String output, String source, String sourceType) {
        var testDriverObservedOutput = new TestDriverObservedOutput();
        testDriverObservedOutput.output = output;
        testDriverObservedOutput.source = source;
        testDriverObservedOutput.sourceType = sourceType;
        return testDriverObservedOutput;
    }
}
