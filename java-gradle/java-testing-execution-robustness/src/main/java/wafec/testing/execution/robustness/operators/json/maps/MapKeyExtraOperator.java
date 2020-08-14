package wafec.testing.execution.robustness.operators.json.maps;

import org.apache.commons.lang3.RandomStringUtils;
import wafec.testing.execution.robustness.CouldNotApplyOperatorException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MapKeyExtraOperator extends AbstractMapOperator {
    public static final String NAME = MapKeyExtraOperator.class.getSimpleName();

    private Random rand = new Random();

    public MapKeyExtraOperator() {
        super();
        name = NAME;
    }

    @Override
    protected Map<String, Object> mutateObject(Map<String, Object> value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        String keyName = null;
        if (value.size() > 0) {
            keyName = RandomStringUtils.randomAlphabetic(rand.nextInt(new ArrayList<String>(value.keySet()).size()));
        } else {
            keyName = RandomStringUtils.randomAlphabetic(1);
        }
        value.put(keyName, null);
        return value;
    }
}
