package wafec.testing.execution.app.commandline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import wafec.testing.execution.EnvironmentController;
import wafec.testing.execution.TestCaseRepository;
import wafec.testing.execution.openstack.OpenStackTestDriver;
import wafec.testing.support.virtualbox.VirtualBoxController;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "openstack")
@Component
public class TestingExecutionOpenStack implements Callable<Integer> {
    @CommandLine.Option(paramLabel = "TEST-CASE-ID", names = { "-t", "--test" }, required = true)
    private long testCaseId;
    @CommandLine.Option(names = { "-e", "--environment" })
    private Long environmentId;

    @Autowired
    private OpenStackTestDriver openStackTestDriver;
    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private ApplicationContext context;

    @Override
    public Integer call() throws Exception {
        EnvironmentController environmentController =  null;
        if (environmentId != null) {
            environmentController = context.getBean(VirtualBoxController.class, environmentId);
        }
        var testCase = testCaseRepository.findById(testCaseId).orElseThrow(IllegalArgumentException::new);
        openStackTestDriver.setEnvironmentController(environmentController);
        openStackTestDriver.runTest(testCase);
        return 0;
    }
}
