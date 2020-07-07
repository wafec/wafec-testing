package wafec.testing.execution;

import lombok.Data;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
public class TestDriverContext {
    private AbstractTestDriver testDriver;
    private final Map<String, Object> viewData = new HashMap<>();

    public TestDriverContext(AbstractTestDriver testDriver) {
        this.testDriver = testDriver;
    }

    public <T> Optional<T> get(String name) {
        T result = null;
        if (viewData.containsKey(name))
            result = (T) viewData.get(name);
        return Optional.ofNullable(result);
    }

    public void set(String name, Object value) {
        viewData.put(name, value);
    }
}
