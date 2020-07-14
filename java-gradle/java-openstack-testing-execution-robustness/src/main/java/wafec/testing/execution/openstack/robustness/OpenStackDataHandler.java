package wafec.testing.execution.openstack.robustness;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import wafec.testing.execution.robustness.ApplicationData;
import wafec.testing.execution.robustness.DataHandler;
import wafec.testing.execution.robustness.DataParseException;

import java.nio.charset.StandardCharsets;

@Component
public class OpenStackDataHandler implements DataHandler {
    @Override
    public ApplicationData handle(byte[] raw) throws DataParseException {
        String content = new String(raw, StandardCharsets.UTF_8);
        ApplicationData result = new ApplicationData();
        result.setRaw(raw);
        return result;
    }
}
