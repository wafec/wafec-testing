package wafec.testing.execution.openstack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
public class ResourceNotCreatedException extends OpenStackTestDriverExecutionException {
    @Getter
    private String resourceName;

    @Override
    public String toString() {
        if (StringUtils.isEmpty(resourceName))
            return "Resource not created";
        return String.format("Resource with name '%s' not created", resourceName);
    }
}
