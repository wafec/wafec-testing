package wafec.testing.execution.app;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import wafec.testing.core.AnalysisBuildJsonObject;
import wafec.testing.core.AnalysisBuildJsonObjectRepository;
import wafec.testing.core.JsonReBuilder;
import wafec.testing.driver.openstack.client.*;
import wafec.testing.execution.*;
import wafec.testing.execution.openstack.*;
import wafec.testing.execution.openstack.robustness.OpenStackRobustnessTestRunner;
import wafec.testing.execution.robustness.RobustnessTest;
import wafec.testing.execution.robustness.RobustnessTestRepository;
import wafec.testing.execution.robustness.SameResultTamper;
import wafec.testing.support.rabbitmq.RabbitMqDataInterception;
import wafec.testing.support.rabbitmq.management.RabbitMqManagementClient;
import wafec.testing.support.virtualbox.VirtualBoxController;
import wafec.testing.support.virtualbox.VirtualBoxMachine;
import wafec.testing.support.virtualbox.VirtualBoxMachineRepository;

@Configuration
@EnableJpaRepositories(basePackageClasses = {
        TestCaseRepository.class,
        KeyRepository.class,
        RobustnessTestRepository.class,
        VirtualBoxMachineRepository.class,
        AnalysisBuildJsonObjectRepository.class
})
@EntityScan(basePackageClasses = {
        TestCase.class,
        Key.class,
        RobustnessTest.class,
        VirtualBoxMachine.class,
        AnalysisBuildJsonObject.class
})
@ComponentScan(basePackageClasses = {
        App.class,
        OpenStackTestDriver.class,
        TestOutputMapper.class,
        OpenStackRobustnessTestRunner.class,
        RabbitMqDataInterception.class,
        SameResultTamper.class,
        VirtualBoxController.class,
        JsonReBuilder.class
})
@EnableFeignClients(basePackageClasses = {
        FlavorClient.class,
        RabbitMqManagementClient.class
})
@EnableTransactionManagement
public class AppConfig {
}
