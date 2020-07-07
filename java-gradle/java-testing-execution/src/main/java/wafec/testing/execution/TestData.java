package wafec.testing.execution;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class TestData {
    private String name;
    private List<TestDataValue> values;

    public Optional<TestDataValue> get(String name) throws TestDataValueNotFoundException {
        for (TestDataValue value : values) {
            if (Optional.of(value).map(TestDataValue::getName).equals(Optional.of(name))) {
                return Optional.of(value);
            }
        }
        throw new TestDataValueNotFoundException(name);
    }

    public Optional<String> getValueOpt(String name) throws TestDataValueNotFoundException {
        return get(name).map(TestDataValue::getValue);
    }

    public String getValue(String name) throws TestDataValueNotFoundException {
        return getValueOpt(name).orElseThrow(IllegalArgumentException::new);
    }
}
