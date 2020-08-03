package wafec.testing.execution.robustness.operators.jsonBased.lists;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListOperatorUtils {
    public static List<ListOperator> getOperators() {
        return Arrays.asList(
                new ListEmptyOperator(),
                new ListItemExtraOperator(),
                new ListItemMissingOperator(),
                new ListNullOperator()
        );
    }

    private static boolean isArray(Object data) {
        return (data != null && data.getClass().isArray());
    }

    public static boolean isApplicable(Object data) {
        return data instanceof List || isArray(data);
    }

    public static List<Object> convert(Object data) throws CouldNotApplyOperatorException {
        if (!isApplicable(data))
            throw new CouldNotApplyOperatorException("Invalid argument");
        List<Object> result = new ArrayList<>();
        if (data != null) {
            if (!isArray(data)) {
                result.addAll((List) data);
            } else {
                for (int i = 0; i < Array.getLength(data); i++) {
                    var value = Array.get(data, i);
                    result.add(value);
                }
            }
        }
        return result;
    }
}
