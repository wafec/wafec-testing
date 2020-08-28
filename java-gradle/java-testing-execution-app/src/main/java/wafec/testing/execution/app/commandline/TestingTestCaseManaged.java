package wafec.testing.execution.app.commandline;

import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine.*;
import wafec.testing.execution.TestCase;
import wafec.testing.execution.TestCaseManagedState;
import wafec.testing.execution.TestCaseManagedStateRepository;
import wafec.testing.execution.TestCaseRepository;

import java.util.concurrent.Callable;

@Command(name = "managed")
public class TestingTestCaseManaged implements Callable<Integer> {
    @Autowired
    TestCaseManagedStateRepository testCaseManagedStateRepository;
    @Autowired
    TestCaseRepository testCaseRepository;

    @Option(names = { "-i", "--id" }, required = true)
    long id;
    @Option(names = { "-s", "--state" })
    String[] stateList;
    @Option(names = { "-c", "--clean" })
    boolean clean;
    @Option(names = {"-d", "--delete" })
    boolean delete;

    @Override
    public Integer call() throws Exception {
        TestCase testCase = testCaseRepository.findById(id).orElseThrow();

        if (clean) {
            var testCaseManagedStateList = testCaseManagedStateRepository.findByTestCase(testCase);
            testCaseManagedStateRepository.deleteAll(testCaseManagedStateList);
            System.out.println("Clean completed");
        }

        if (stateList != null && stateList.length > 0)
            for (var stateName : stateList) {
                var testCaseManagedState = testCaseManagedStateRepository.findByTestCaseAndState(testCase, stateName);
                if (!delete) {
                    if (testCaseManagedState == null) {
                        testCaseManagedState = new TestCaseManagedState();
                        testCaseManagedState.setName(stateName);
                        testCaseManagedState.setTestCase(testCase);
                        testCaseManagedStateRepository.save(testCaseManagedState);
                        System.out.println(String.format("Managed State Created #%d [%s]", testCaseManagedState.getId(), stateName));
                    }
                } else if (testCaseManagedState != null) {
                    System.out.println(String.format("Managed State Deleted #%d [%s]", testCaseManagedState.getId(), stateName));
                    testCaseManagedStateRepository.delete(testCaseManagedState);
                }
            }
        else {
            var testCaseManagedStateList = testCaseManagedStateRepository.findByTestCase(testCase);
            System.out.println(String.format("## Managed State List for Test Case #%d", id));
            for (var testCaseManagedState : testCaseManagedStateList) {
                System.out.println(String.format("  - %s [%d]", testCaseManagedState.getName(), testCaseManagedState.getId()));
            }
        }
        return 0;
    }
}
