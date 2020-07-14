package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "networks", url = "${wafec.testing.driver.openstack.client.networks}", configuration = CustomOpenStackClientConfiguration.class)
public interface NetworkClient extends CustomOpenStackCrudClient<Network> {
}
