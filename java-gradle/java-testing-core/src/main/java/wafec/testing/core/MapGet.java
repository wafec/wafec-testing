package wafec.testing.core;

import lombok.AllArgsConstructor;

import java.util.Map;

public class MapGet {
    private Map<String, Object> map;

    public MapGet(Map<String, Object> map) {
        this.map = map;
    }

    public <T> T get(String name) {
        if (!map.containsKey(name))
            throw new IllegalArgumentException(String.format("Value labeled with '%s' not found", name));
        return (T) map.get(name);
    }

    public static MapGet of(Map<String, Object> map) {
        return new MapGet(map);
    }
}
