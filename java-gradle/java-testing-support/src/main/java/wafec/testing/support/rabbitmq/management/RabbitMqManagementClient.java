package wafec.testing.support.rabbitmq.management;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "rabbit-management", url = "${wafec.testing.support.rabbitmq.management.client.url}", configuration = RabbitMqClientConfiguration.class)
public interface RabbitMqManagementClient {
    @RequestMapping(value = "/bindings/%2F", method = RequestMethod.GET)
    List<BindingView> getBindings();
    @RequestMapping(value = "/bindings/{vhost}", method = RequestMethod.GET)
    List<BindingView> getBindings(@PathVariable("vhost") String virtualHost);
    @RequestMapping(value = "/exchanges/{vhost}/{name}", method = RequestMethod.GET)
    ExchangeView getExchange(@PathVariable("vhost") String virtualHost, @PathVariable("name") String name);
    @RequestMapping(value = "/exchanges/%2F/{name}", method = RequestMethod.GET)
    ExchangeView getExchange(@PathVariable("name") String name);
    @RequestMapping(value = "/vhosts", method = RequestMethod.GET)
    List<VirtualHostView> getVirtualHosts();
    @RequestMapping(value = "/queues/%2F", method = RequestMethod.GET)
    List<QueueView> getQueues();
    @RequestMapping(value = "/queues/{vhost}", method = RequestMethod.GET)
    List<QueueView> getQueues(@PathVariable("vhost") String virtualHost);
    @RequestMapping(value = "/exchanges/%2F", method = RequestMethod.GET)
    List<ExchangeView> getExchanges();
    @RequestMapping(value = "/exchanges/{vhost}", method = RequestMethod.GET)
    List<ExchangeView> getExchanges(String virtualHost);
    @RequestMapping(value = "/queues/%2F/{name}", method = RequestMethod.GET)
    QueueView getQueue(@PathVariable("name") String name);
    @RequestMapping(value = "/queues/{vhost}/{name}", method = RequestMethod.GET)
    QueueView getQueue(@PathVariable("vhost") String virtualHost, @PathVariable("name") String name);
}
