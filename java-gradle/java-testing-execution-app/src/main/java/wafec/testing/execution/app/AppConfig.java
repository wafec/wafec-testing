package wafec.testing.execution.app;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import wafec.testing.driver.openstack.client.*;
import wafec.testing.execution.*;
import wafec.testing.execution.openstack.*;
import wafec.testing.execution.openstack.robustness.OpenStackRobustnessTestRunner;
import wafec.testing.execution.robustness.DummyDataCorruption;
import wafec.testing.support.rabbitmq.RabbitMqDataInterception;
import wafec.testing.support.rabbitmq.management.RabbitMqManagementClient;

@Configuration
@EnableJpaRepositories(basePackageClasses = {
        TestCaseRepository.class,
        KeyRepository.class
})
@EntityScan(basePackageClasses = {
        TestCase.class,
        Key.class
})
@ComponentScan(basePackageClasses = {
        App.class,
        OpenStackTestDriver.class,
        TestOutputMapper.class,
        OpenStackRobustnessTestRunner.class,
        RabbitMqDataInterception.class,
        DummyDataCorruption.class
})
@EnableFeignClients(basePackageClasses = {
        FlavorClient.class,
        RabbitMqManagementClient.class
})
@EnableTransactionManagement
public class AppConfig {
}
