package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "flavors", url = "${flavors.url}")
public interface FlavorClient extends CrudClient<Flavor> {

}
