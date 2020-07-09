package wafec.testing.execution.openstack.modules;

import wafec.testing.driver.openstack.client.Server;
import wafec.testing.execution.TestDataValueNotFoundException;
import wafec.testing.execution.TestDriverException;

public interface OpenStackModuleActionConsumer<T> {
    void accept(T resource) throws TestDataValueNotFoundException, TestDriverException;
}
