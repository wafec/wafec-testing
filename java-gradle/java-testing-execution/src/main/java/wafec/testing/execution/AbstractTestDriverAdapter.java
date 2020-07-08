package wafec.testing.execution;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractTestDriverAdapter extends AbstractTestDriver {
    private final TestDriverInputModelBuilder inputModel;
    @Autowired
    private TestDataMapper testDataMapper;

    public AbstractTestDriverAdapter() {
        super();
        this.configure(inputModel = new TestDriverInputModelBuilder());
    }

    @Override
    protected List<TestDriverObservedOutput> runTestInput(TestInput testInput, TestExecution testExecution) throws TestDriverInputNotFoundException,
            TestDataValueNotFoundException, PreConditionViolationException, TestDriverException {
        var function = inputModel.get(testInput);
        var testData = testDataMapper.fromTestInput(testInput);
        List<TestDriverObservedOutput> result = new ArrayList<>();
        if (function.getClass().isAnnotationPresent(PreCondition.class)) {
            var preCondition = function.getClass().getAnnotation(PreCondition.class);
            var target = preCondition.target();
            var targetFunction = inputModel.get(target);
            result.addAll(targetFunction.apply(TestDriverInputFunctionHandler.of(testData, testInput, testExecution, testDriverContext)));
        }
        result.addAll(function.apply(TestDriverInputFunctionHandler.of(testData, testInput, testExecution, testDriverContext)));
        return result;
    }

    @Override
    public List<AcceptedInput> getAcceptedInputs() {
        return inputModel.buildSignatureList().stream()
                .map(AcceptedInput::new).collect(Collectors.toList());
    }

    protected abstract void configure(TestDriverInputModelBuilder builder);

    protected void configurePost(Consumer<TestDriverInputModelBuilder> builderConsumer) {
        builderConsumer.accept(inputModel);
    }
}
