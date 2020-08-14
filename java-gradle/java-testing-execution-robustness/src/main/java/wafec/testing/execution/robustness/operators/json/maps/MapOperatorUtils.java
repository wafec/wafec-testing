package wafec.testing.execution.robustness.operators.json.maps;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapOperatorUtils {
    public static List<MapOperator> getOperators() {
        return Arrays.asList(
                new MapEmptyOperator(),
                new MapKeyExtraOperator(),
                new MapKeyMissingOperator(),
                new MapNullOperator()
        );
    }

    public static boolean isApplicable(Object data) {
        return data instanceof Map;
    }
}
