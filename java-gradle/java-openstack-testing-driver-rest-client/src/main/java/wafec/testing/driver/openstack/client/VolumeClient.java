package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "volumes", url = "${volumes.url}", configuration = CustomOpenStackClientConfiguration.class)
public interface VolumeClient extends CustomOpenStackCrudClient<Volume> {
}
