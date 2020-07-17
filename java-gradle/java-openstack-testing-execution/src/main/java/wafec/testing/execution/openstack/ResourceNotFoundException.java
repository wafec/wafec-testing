package wafec.testing.execution.openstack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
@AllArgsConstructor
public class ResourceNotFoundException extends OpenStackTestDriverExecutionException {
    @Getter
    private String resourceName;

    @Override
    public String toString() {
        if (StringUtils.isEmpty(resourceName))
            return "Resource not found";
        return String.format("Resource of name '%s' not found", resourceName);
    }
}
