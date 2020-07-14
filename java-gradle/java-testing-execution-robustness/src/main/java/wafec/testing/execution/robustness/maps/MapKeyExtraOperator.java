package wafec.testing.execution.robustness.maps;

import org.apache.commons.lang3.RandomStringUtils;
import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.HashMap;

public class MapKeyExtraOperator extends AbstractMapOperator {
    public static final String NAME = MapKeyExtraOperator.class.getSimpleName();

    public MapKeyExtraOperator() {
        super();
        name = NAME;
    }

    @Override
    protected HashMap<String, Object> mutateObject(HashMap<String, Object> value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        value.put(RandomStringUtils.randomAlphabetic(1), null);
        return value;
    }
}
