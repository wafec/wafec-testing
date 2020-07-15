package wafec.testing.core;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

@Data
public class JsonReBuilderObject {
    private JsonNode node;
    private JsonNode parent;
    private String fieldName;
    private String context;
    private Integer index;
    private JsonNode result;

    public static JsonReBuilderObject of(JsonNode parent, JsonNode node, String fieldName, String context) {
        JsonReBuilderObject object = new JsonReBuilderObject();
        object.setParent(parent);
        object.setNode(node);
        object.setFieldName(fieldName);
        object.setResult(node);
        object.setContext(context);
        return object;
    }

    public static JsonReBuilderObject of(JsonNode parent, JsonNode node, int index, String context) {
        JsonReBuilderObject object = new JsonReBuilderObject();
        object.setParent(parent);
        object.setNode(node);
        object.setIndex(index);
        object.setResult(node);
        object.setContext(context);
        return object;
    }

    @Override
    public String toString() {
        if (StringUtils.isNotEmpty(fieldName)) {
            return String.format("%s.%s", context, fieldName);
        } else {
            return String.format("%s[%d]", context, index);
        }
    }
}
