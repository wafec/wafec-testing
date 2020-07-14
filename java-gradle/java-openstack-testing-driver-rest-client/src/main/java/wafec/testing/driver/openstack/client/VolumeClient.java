package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "volumes", url = "${wafec.testing.driver.openstack.client.volumes}", configuration = CustomOpenStackClientConfiguration.class)
public interface VolumeClient extends CustomOpenStackCrudClient<Volume> {
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = "application/json")
    void extend(@RequestParam("key") String key, @PathVariable("id") String id, @RequestBody Volume volumeExtend);
}
