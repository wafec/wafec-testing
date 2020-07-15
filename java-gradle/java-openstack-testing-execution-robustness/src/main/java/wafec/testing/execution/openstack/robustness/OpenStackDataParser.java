package wafec.testing.execution.openstack.robustness;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import wafec.testing.execution.robustness.ApplicationData;
import wafec.testing.execution.robustness.DataParser;
import wafec.testing.execution.robustness.DataParseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class OpenStackDataParser implements DataParser {
    @Override
    public ApplicationData handle(byte[] body) throws DataParseException {
        try {
            OpenStackApplicationData result = new OpenStackApplicationData();
            result.setData(body);
            result.setBodyNode(parseJson(body));
            return result;
        } catch (IOException exc) {
            throw new DataParseException("Error while parsing json from body", exc);
        }
    }

    private JsonNode parseJson(byte[] body) throws IOException {
        String content = new String(body, StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(content);
    }
}
