package wafec.testing.execution;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestDriverException extends Exception {
    @Getter
    private List<TestDriverObservedOutput> observedOutputsOnFail = new ArrayList<>();

    public TestDriverException() {
        super();
    }

    public TestDriverException(String message) {
        super(message);
    }

    public TestDriverException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public TestDriverException addAll(List<TestDriverObservedOutput> observedOutputs) {
        this.observedOutputsOnFail.addAll(observedOutputs);
        this.sortOutputs();
        return this;
    }

    public TestDriverException addAll(TestDriverObservedOutputBuilder builder) {
        this.observedOutputsOnFail.addAll(builder.buildList());
        this.sortOutputs();
        return this;
    }

    private void sortOutputs() {
        this.observedOutputsOnFail.sort(Comparator.comparing(TestDriverObservedOutput::getCreatedAt));
    }
}
