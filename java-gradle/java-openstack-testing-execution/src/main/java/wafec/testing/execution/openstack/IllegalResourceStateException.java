package wafec.testing.execution.openstack;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IllegalResourceStateException extends OpenStackTestDriverException {
    @Getter
    private String resourceName;

    public IllegalResourceStateException(String resourceName) {
        super(resourceName);
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return String.format("Resource '%s' encounters in illegal state", resourceName);
    }
}
