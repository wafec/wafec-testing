package wafec.testing.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {
    public static Object getValue(JsonNode node) {
        if (node instanceof IntNode) {
            return node.asInt();
        }
        if (node instanceof LongNode) {
            return node.asLong();
        }
        if (node instanceof FloatNode || node instanceof DoubleNode) {
            return node.asDouble();
        }
        if (node instanceof TextNode) {
            return node.asText();
        }
        if (node instanceof BooleanNode) {
            return node.asBoolean();
        }
        if (node instanceof ArrayNode) {
            ArrayList<Object> result = new ArrayList<>();
            for (int i = 0; i < node.size(); i++)
                result.add(getValue(node.get(i)));
            return result;
        }
        if (node instanceof ObjectNode) {
            Map<String, Object> result = new HashMap<>();
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                result.put(fieldName, node.get(fieldName));
            }
            return result;
        }
        return node;
    }
}
