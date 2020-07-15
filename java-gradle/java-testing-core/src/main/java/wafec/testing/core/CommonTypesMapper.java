package wafec.testing.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonTypesMapper {
    public static CommonTypes fromJsonNode(JsonNode jsonNode) {
        var nodeType = jsonNode.getNodeType();
        if (nodeType.equals(JsonNodeType.BOOLEAN))
            return CommonTypes.BOOLEAN;
        if (nodeType.equals(JsonNodeType.STRING))
            return CommonTypes.STRING;
        if (nodeType.equals(JsonNodeType.NUMBER))
            return CommonTypes.NUMBER;
        if (nodeType.equals(JsonNodeType.OBJECT))
            return CommonTypes.OBJECT;
        if (nodeType.equals(JsonNodeType.ARRAY))
            return CommonTypes.LIST;
        return CommonTypes.UNKNOWN;
    }
}
