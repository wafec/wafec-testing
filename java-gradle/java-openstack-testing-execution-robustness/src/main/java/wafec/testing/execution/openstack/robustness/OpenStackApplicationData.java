package wafec.testing.execution.openstack.robustness;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import wafec.testing.execution.robustness.ApplicationData;

@Data
@EqualsAndHashCode(callSuper = true)
public class OpenStackApplicationData extends ApplicationData {
    private JsonNode bodyNode;
}
