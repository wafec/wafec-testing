package wafec.testing.execution.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;
import wafec.testing.execution.*;
import wafec.testing.execution.app.models.TestArgumentItem;
import wafec.testing.execution.app.models.TestCaseImportModel;
import wafec.testing.execution.app.models.TestCaseItem;
import wafec.testing.execution.app.models.TestInputItem;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Callable;

@Command(name = "import")
@Component
public class TestingImport implements Callable<Integer> {
    @Parameters(paramLabel = "FILE")
    private File[] files;
    @Autowired
    private TestCaseRepository testCaseRepository;
    @Autowired
    private TestInputRepository testInputRepository;
    @Autowired
    private TestInputParameterRepository testInputParameterRepository;
    @Autowired
    private TestInputParameterDataRepository testInputParameterDataRepository;

    @Override
    public Integer call() throws Exception {
        for (File file : files)
            importTest(file);
        return 0;
    }

    public void importTest(File file) {
        if (!file.getName().endsWith(".yaml"))
            throw new IllegalArgumentException("Required YAML file");
        try {
            var model = TestCaseImportModel.parseYAML(file);
            save(model);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Transactional
    private void save(TestCaseImportModel model) throws Exception {
        if (model.getImportId() == null) {
            var testCaseItem = Optional.of(model).map(TestCaseImportModel::getTestCase).get();
            var importId = save(testCaseItem);
            model.setImportId(importId);
            model.save();
        }
    }

    private Long save(TestCaseItem testCaseItem) {
        TestCase testCase = new TestCase();
        testCase.setTargetSystem(testCaseItem.getTargetSystem());
        testCase.setDescription(testCaseItem.getDescription());
        testCase.setCreatedAt(new Date());
        System.out.println(String.format("Instance: %s", testCaseRepository));
        testCaseRepository.save(testCase);

        for(int i = 0; i < testCaseItem.getTestInputs().size(); i++) {
            var testInputItem = testCaseItem.getTestInputs().get(i);
            save(testInputItem, testCase, i);
        }

        return testCase.getId();
    }

    private void save(TestInputItem testInputItem, TestCase testCase, int position) {
        TestInput testInput = new TestInput();
        testInput.setTestCase(testCase);
        testInput.setName(testInputItem.getName());
        testInput.setPosition(position);
        testInput.setSignature(testInputItem.getSignature());

        testInputRepository.save(testInput);

        for (var testArgumentItem : testInputItem.getTestArguments()) {
            save(testArgumentItem, testInput);
        }
    }

    private void save(TestArgumentItem testArgumentItem, TestInput testInput) {
        TestInputParameter parameter = new TestInputParameter();
        parameter.setName(testArgumentItem.getName());
        parameter.setTestInput(testInput);
        parameter.setParamType(testArgumentItem.getArgType());

        TestInputParameterData data = new TestInputParameterData();
        data.setTestInputParameter(parameter);
        data.setData(testArgumentItem.getValue());

        testInputParameterRepository.save(parameter);
        testInputParameterDataRepository.save(data);
    }
}
