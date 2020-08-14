package wafec.testing.execution.robustness.operators.json.booleans;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BooleanOperatorUtils {
    public static boolean isApplicable(Object data) {
        return data instanceof Boolean;
    }

    public static List<BooleanOperator> getOperators() {
        return Arrays.asList(
            new BooleanFalseOperator(),
            new BooleanNegateOperator(),
            new BooleanNullOperator(),
            new BooleanTrueOperator()
        );
    }
}
