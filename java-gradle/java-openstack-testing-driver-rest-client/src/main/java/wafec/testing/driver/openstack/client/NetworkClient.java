package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "networks", url = "${networks.url}")
public interface NetworkClient extends CustomCrudClient<Network> {
}
