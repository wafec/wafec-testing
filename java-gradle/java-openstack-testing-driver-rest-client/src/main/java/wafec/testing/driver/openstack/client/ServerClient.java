package wafec.testing.driver.openstack.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "servers", url = "${wafec.testing.driver.openstack.client.servers}", configuration = CustomOpenStackClientConfiguration.class)
public interface ServerClient extends CustomOpenStackCrudClient<Server> {
    @RequestMapping(value = "/{id}/actions/pause", method = RequestMethod.POST)
    void pause(@RequestParam("key") String key, @PathVariable("id") String id);
    @RequestMapping(value = "/{id}/actions/unpause", method = RequestMethod.POST)
    void unpause(@RequestParam("key") String key, @PathVariable("id") String id);
    @RequestMapping(value = "/{id}/actions/shelve", method = RequestMethod.POST)
    void shelve(@RequestParam("key") String key, @PathVariable("id") String id);
    @RequestMapping(value = "/{id}/actions/unshelve", method = RequestMethod.POST)
    void unshelve(@RequestParam("key") String key, @PathVariable("id") String id);
    @RequestMapping(value = "/{id}/actions/stop", method = RequestMethod.POST)
    void stop(@RequestParam("key") String key, @PathVariable("id") String id);
    @RequestMapping(value = "/{id}/actions/start", method = RequestMethod.POST)
    void start(@RequestParam("key") String key, @PathVariable("id") String id);
    @RequestMapping(value = "/{id}/actions/suspend", method = RequestMethod.POST)
    void suspend(@RequestParam("key") String key, @PathVariable("id") String id);
    @RequestMapping(value = "/{id}/actions/resume", method = RequestMethod.POST)
    void resume(@RequestParam("key") String key, @PathVariable("id") String id);
    @RequestMapping(value = "/{id}/actions/resize", method = RequestMethod.POST, consumes = "application/json")
    void resize(@RequestParam("key") String key, @PathVariable("id") String id, @RequestBody Resource flavor);
    @RequestMapping(value = "/{id}/actions/resize/confirm", method = RequestMethod.POST)
    void resizeConfirm(@RequestParam("key") String key, @PathVariable("id") String id, @RequestParam("action") String action);
    @RequestMapping(value = "/{id}/actions/migrate", method = RequestMethod.POST)
    void migrate(@RequestParam("key") String key, @PathVariable("id") String id);
    @RequestMapping(value = "/{id}/actions/live-migrate", method = RequestMethod.POST)
    void liveMigrate(@RequestParam("key") String key, @PathVariable("id") String id, @RequestParam("host") String host);
    @RequestMapping(value = "/{id}/migrate/hosts", method = RequestMethod.GET)
    List<String> migrateHosts(@RequestParam("key") String key, @PathVariable("id") String id);
    @RequestMapping(value = "/{id}/actions/rebuild", method = RequestMethod.POST, consumes = "application/json")
    void rebuild(@RequestParam("key") String key, @PathVariable("id") String id, @RequestBody Resource image);
}
