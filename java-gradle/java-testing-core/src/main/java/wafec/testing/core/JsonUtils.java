package wafec.testing.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

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
        return node;
    }
}
