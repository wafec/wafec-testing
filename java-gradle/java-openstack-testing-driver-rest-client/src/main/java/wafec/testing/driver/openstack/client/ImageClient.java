package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "images", url = "${images.url}")
public interface ImageClient extends CrudClient<Image> {

}
