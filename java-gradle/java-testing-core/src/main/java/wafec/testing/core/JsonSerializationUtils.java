package wafec.testing.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonSerializationUtils {
    public static String serialize(Object data) throws IOException {
        if (data == null)
            return null;
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
    }

    public static String trySerialize(Object data, String defaultValue) {
        try {
            return serialize(data);
        } catch (IOException exc) {
            return defaultValue;
        }
    }
}
