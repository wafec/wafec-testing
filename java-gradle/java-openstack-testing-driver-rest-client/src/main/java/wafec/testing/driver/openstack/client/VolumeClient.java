package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "volumes", url = "${volumes.url}")
public interface VolumeClient extends CrudClient<Volume> {
}
