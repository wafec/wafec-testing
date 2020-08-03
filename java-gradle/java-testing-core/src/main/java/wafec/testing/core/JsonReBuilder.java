package wafec.testing.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class JsonReBuilder {
    private JsonNode rootNode;
    private JsonReBuilderFunction builderFunction;

    @Autowired
    private AnalysisBuildJsonObjectRepository analysisBuildJsonObjectRepository;

    @Value("${wafec.testing.core.analysis.json-builder:false}")
    private boolean analyze;

    public JsonReBuilder(JsonNode rootNode) {
        this.rootNode = rootNode;
    }

    public void rebuild(@NonNull JsonReBuilderFunction builderFunction) {
        this.builderFunction = builderFunction;
        AnalysisBuildJsonObject analysisBuildJsonObject = null;
        if (analyze) {
            analysisBuildJsonObject = new AnalysisBuildJsonObject();
            analysisBuildJsonObject.setBeforeData(JsonSerializationUtils.trySerialize(rootNode, null));
            analysisBuildJsonObject.setCreatedAt(new Date());
        }
        visit(rootNode, "JSON");
        if (analyze) {
            analysisBuildJsonObject.setAfterData(JsonSerializationUtils.trySerialize(rootNode, null));
            if (analysisBuildJsonObject.getBeforeData() != null && analysisBuildJsonObject.getAfterData() != null) {
                var left = analysisBuildJsonObject.getBeforeData();
                var right = analysisBuildJsonObject.getAfterData();
                Map<Character, Integer> charCount = new HashMap<>();
                for (int i = 0; i < left.length(); i++) {
                    Character c = left.charAt(i);
                    if (charCount.containsKey(c)) {
                        charCount.put(c, charCount.get(c) + 1);
                    } else {
                        charCount.put(c, 1);
                    }
                }
                for (int i = 0; i < right.length(); i++) {
                    Character c = right.charAt(i);
                    if (charCount.containsKey(c)) {
                        charCount.put(c, charCount.get(c) - 1);
                    } else {
                        charCount.put(c, -1);
                    }
                }
                var score = charCount.values().stream().map(v -> v < 0 ? -v : v).collect(Collectors.summarizingInt(Integer::intValue)).getSum();
                analysisBuildJsonObject.setSimilarityScore(score / (double)(left.length() + right.length()));
                analysisBuildJsonObject.setSimilarityAlgorithm("CustomDifference");
            }
            analysisBuildJsonObjectRepository.save(analysisBuildJsonObject);
        }
    }

    private void visit(JsonNode node, String context) {
        if (node == null)
            return;

        if (node.getNodeType().equals(JsonNodeType.OBJECT)) {
            List<String> fieldNames = new ArrayList<>();
            node.fieldNames().forEachRemaining(fieldNames::add);
            Collections.reverse(fieldNames);

            for (var fieldName : fieldNames) {
                var value = node.get(fieldName);
                var result = builderFunction.apply(JsonReBuilderObject.of(node, value, fieldName, context));

                var objectNode = (ObjectNode) node;
                objectNode.remove(fieldName);
                objectNode.set(fieldName, result);

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
