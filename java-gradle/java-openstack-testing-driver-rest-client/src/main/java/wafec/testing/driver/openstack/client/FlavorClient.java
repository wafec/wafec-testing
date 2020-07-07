package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "flavors", url = "${flavors.url}", configuration = CustomOpenStackClientConfiguration.class)
public interface FlavorClient extends CustomOpenStackCrudClient<Flavor> {

}
