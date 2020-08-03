package wafec.testing.execution.robustness.operators.jsonBased.integers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerOperatorUtils {
    public static List<IntegerOperator> getOperators() {
        return Arrays.asList(
                new IntegerMaxOperator(),
                new IntegerMinOperator(),
                new IntegerMinusOneOperator(),
                new IntegerNullOperator(),
                new IntegerPlusOneOperator(),
                new IntegerZeroMinusOneOperator(),
                new IntegerZeroOperator(),
                new IntegerZeroPlusOneOperator()
        );
    }

    public static boolean isApplicable(Object data) {
        return data instanceof Integer;
    }
}
