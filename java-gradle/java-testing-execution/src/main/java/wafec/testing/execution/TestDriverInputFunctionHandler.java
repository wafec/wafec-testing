package wafec.testing.execution;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TestDriverInputFunctionHandler {
    private TestData testData;
    private TestInput testInput;
    private TestExecution testExecution;
    private TestDriverContext testDriverContext;
}
