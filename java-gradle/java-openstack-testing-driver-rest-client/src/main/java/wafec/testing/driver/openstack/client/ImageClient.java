package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "images", url = "${images.url}", configuration = CustomOpenStackClientConfiguration.class)
public interface ImageClient extends CustomOpenStackCrudClient<Image> {

}
