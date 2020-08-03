package wafec.testing.execution.openstack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
public class ResourceNotCreatedException extends OpenStackTestDriverExecutionException {
    @Getter
    private String resourceName;

    public ResourceNotCreatedException(String resourceName) {
        super(buildCustomMessage(resourceName));
    }

    private String getCustomMessage() {
        return buildCustomMessage(resourceName);
    }

    private static String buildCustomMessage(String resourceName) {
        if (StringUtils.isEmpty(resourceName))
            return "Resource not created";
        return String.format("Resource with name '%s' not created", resourceName);
    }

    @Override
    public String toString() {
        return getCustomMessage();
    }
}
