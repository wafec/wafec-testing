package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "servers", url = "${servers.url}")
public interface ServerClient extends CrudClient<Server> {
}
