package wafec.testing.driver.openstack.client;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CustomOpenStackCrudClient<T> {
    @RequestMapping(method = RequestMethod.POST, value = "/", consumes = "application/json")
    T create(@RequestParam("key") String key, @RequestBody T instance);
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/")
    void delete(@PathVariable("id") String id, @RequestParam("key") String key);
    @RequestMapping(method = RequestMethod.PUT, value = "/")
    T update(@RequestParam("key") String key, @RequestBody T instance);

    @RequestMapping(method = RequestMethod.GET, value="/")
    List<T> getAll(@RequestParam("key") String key);
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    T findById(@PathVariable("id") String id, @RequestParam("key") String key);
    @RequestMapping(method = RequestMethod.GET, value = "/")
    List<T> findByName(@RequestParam("key") String key, @RequestParam("name") String name);
}
