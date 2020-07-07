package wafec.testing.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class TestDataMapper {
    @Autowired
    private TestInputParameterDataRepository testInputParameterDataRepository;

    public TestData fromTestInput(TestInput testInput) {
        var testInputParameterDataList = testInputParameterDataRepository.findByTestInput(testInput);
        var testDataValueList = testInputParameterDataList.stream()
                .map(this::fromTestInputParameterData).collect(Collectors.toList());
        TestData testData = new TestData();
        testData.setName(testInput.getName());
        testData.setValues(testDataValueList);
        return testData;
    }

    public TestDataValue fromTestInputParameterData(TestInputParameterData testInputParameterData) {
        var testDataValue = new TestDataValue();
        testDataValue.setName(testInputParameterData.getTestInputParameter().getName());
        testDataValue.setValue(testInputParameterData.getData());
        testDataValue.setArgType(testInputParameterData.getTestInputParameter().getParamType());
        return testDataValue;
    }
}
