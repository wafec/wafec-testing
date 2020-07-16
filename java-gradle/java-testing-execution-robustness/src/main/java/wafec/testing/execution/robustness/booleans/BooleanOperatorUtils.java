package wafec.testing.execution.robustness.booleans;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
