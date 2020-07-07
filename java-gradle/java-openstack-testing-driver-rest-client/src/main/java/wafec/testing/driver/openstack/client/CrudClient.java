package wafec.testing.driver.openstack.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CrudClient<T> {
    @RequestMapping(method = RequestMethod.GET, value = "?key={key}")
    List<Flavor> getAll(@RequestParam String key);
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/?key={key}")
    Flavor findById(@PathVariable String id, @RequestParam String key);
    @RequestMapping(method = RequestMethod.GET, value = "/?key={key}&name={name}")
    List<Flavor> findByName(@RequestParam String key, @RequestParam String name);
    @RequestMapping(method = RequestMethod.POST, value = "?key={key}", consumes = "application/json")
    Flavor create(@RequestParam String key, T instance);
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}/?key={key}")
    void delete(@PathVariable String id, @RequestParam String key);
    @RequestMapping(method = RequestMethod.PUT, value = "?key={key}")
    T update(@RequestParam String key, T instance);
}
