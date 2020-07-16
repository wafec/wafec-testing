package wafec.testing.execution.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import lombok.ToString;

import java.io.File;
import java.io.IOException;

@Data
@ToString
public class TestCaseImportModel {
    @JsonIgnore
    private File file;
    @JsonProperty(value = "import-id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long importId;
    @JsonProperty("test-case")
    private TestCaseItem testCase;

    public static TestCaseImportModel parseYAML(File file) throws
            JsonParseException, JsonMappingException, IOException {
        var mapper = new ObjectMapper(new YAMLFactory());
        var model = mapper.readValue(file, TestCaseImportModel.class);
        model.setFile(file);
        return model;
    }

    public void save() throws
            JsonGenerationException, IOException {
        var mapper = new ObjectMapper(new YAMLFactory());
        mapper.writeValue(file, this);
    }
}
