package wafec.testing.execution.robustness.strings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringOperatorUtils {
    public static List<StringOperator> getOperators() {
        return Arrays.asList(
                new StringAlphabeticOperator(),
                new StringEmptyOperator(),
                new StringNullOperator(),
                new StringNumericOperator(),
                new StringOverflowOperator(),
                new StringRandomOperator()
        );
    }

    public static boolean isApplicable(Object data) {
        return data instanceof String;
    }
}
