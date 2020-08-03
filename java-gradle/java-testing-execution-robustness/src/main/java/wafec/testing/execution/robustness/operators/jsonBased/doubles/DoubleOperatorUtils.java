package wafec.testing.execution.robustness.operators.jsonBased.doubles;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DoubleOperatorUtils {
    public static List<DoubleOperator> getOperators() {
        return Arrays.asList(
                new DoubleMaxOperator(),
                new DoubleMinOperator(),
                new DoubleNullOperator(),
                new DoublePlusOneOperator(),
                new DoubleZeroMinusOneOperator(),
                new DoubleZeroOperator(),
                new DoubleZeroPlusOneOperator()
        );
    }

    public static boolean isApplicable(Object data) {
        return data instanceof Double;
    }
}
