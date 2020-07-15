package wafec.testing.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class JsonReBuilder {
    private JsonNode rootNode;
    private JsonReBuilderFunction builderFunction;

    public JsonReBuilder(JsonNode rootNode) {
        this.rootNode = rootNode;
    }

    public void rebuild(@NonNull JsonReBuilderFunction builderFunction) {
        this.builderFunction = builderFunction;
        visit(rootNode, "JSON");
    }

    private void visit(JsonNode node, String context) {
        if (node == null)
            return;
        List<String> fieldNames = new ArrayList<>();
        node.fieldNames().forEachRemaining(fieldNames::add);
        if (node.getNodeType().equals(JsonNodeType.OBJECT)) {
            for (var fieldName : fieldNames) {
                var value = node.get(fieldName);
                var result = builderFunction.apply(JsonReBuilderObject.of(node, value, fieldName, context));
                if (value != result) {
                    var objectNode = (ObjectNode) node;
                    objectNode.remove(fieldName);
                    objectNode.set(fieldName, result);
                }
                visit(result, String.format("%s.%s", context, fieldName));
            }
        } else if (node.getNodeType().equals(JsonNodeType.ARRAY)) {
            for (var i = 0; i < node.size(); i++) {
                var value = node.get(i);
                var result = builderFunction.apply(JsonReBuilderObject.of(node, value, i, context));
                if (!value.asText().equals(result.asText())) {
                    var arrayNode = (ArrayNode) node;
                    arrayNode.remove(i);
                    arrayNode.insert(i, result);
                }
                visit(result, String.format("%s[%d]", context, i));
            }
        }
    }
}
