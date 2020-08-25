package wafec.testing.execution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestDriverObservedOutputBuilder {
    public TestDriverObservedOutput instance;
    private final List<TestDriverObservedOutput> list = new ArrayList<>();

    public TestDriverObservedOutputBuilder() {

    }

    public TestDriverObservedOutputBuilder setOutput(String output) {
        instance.setOutput(output);
        return this;
    }

    public TestDriverObservedOutput build() {
        if (instance == null)
            throw new IllegalStateException("Instance initialization failed");
        return instance;
    }

    public List<TestDriverObservedOutput> buildList() {
        if (instance != null)
            list.add(instance);
        list.sort(Comparator.comparing(TestDriverObservedOutput::getCreatedAt));
        return new ArrayList<>(list);
    }

    public TestDriverObservedOutputBuilderFactory and() {
        if (instance != null)
            list.add(instance);
        return new TestDriverObservedOutputBuilderFactory(this);
    }

    public TestDriverObservedOutputBuilder join(List<TestDriverObservedOutput> observedOutputs) {
        list.addAll(observedOutputs);
        return this;
    }

    public static TestDriverObservedOutputBuilderFactory startBuild() {
        TestDriverObservedOutputBuilder builder = new TestDriverObservedOutputBuilder();
        return builder.factory();
    }

    public TestDriverObservedOutputBuilderFactory factory() {
        return new TestDriverObservedOutputBuilderFactory(this);
    }

}
