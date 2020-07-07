package wafec.testing.execution.app;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import wafec.testing.driver.openstack.client.*;
import wafec.testing.execution.*;

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
        FlavorClient.class
})
public class AppConfig {
}
