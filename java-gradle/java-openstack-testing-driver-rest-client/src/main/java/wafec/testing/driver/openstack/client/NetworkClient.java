package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "networks", url = "${networks.url}", configuration = CustomOpenStackClientConfiguration.class)
public interface NetworkClient extends CustomOpenStackCrudClient<Network> {
}
