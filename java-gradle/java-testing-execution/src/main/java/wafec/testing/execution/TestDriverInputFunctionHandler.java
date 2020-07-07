package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TestDriverInputFunctionHandler {
    private TestData testData;
    private TestInput testInput;
    private TestExecution testExecution;
}
