package wafec.testing.core;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Function;

public interface JsonReBuilderFunction extends Function<JsonReBuilderObject, JsonNode> {
}
