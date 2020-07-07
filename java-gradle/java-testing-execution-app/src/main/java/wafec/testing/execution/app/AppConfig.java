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
        TestOutputMapper.class
})
@EnableFeignClients(basePackageClasses = {
    FlavorClient.class
})
@EnableTransactionManagement
public class AppConfig {
}
