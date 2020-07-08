package wafec.testing.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDriverObservedOutputBuilderFactory {
    public static final String ERROR_TYPE = "error";
    public static final String LOG_TYPE = "log";
    public static final String SUCCESS_TYPE = "success";
    public static final String CHANGE_TYPE = "change";

    private TestDriverObservedOutputBuilder builder;

    Logger logger = LoggerFactory.getLogger(TestDriverObservedOutputBuilderFactory.class);

    public TestDriverObservedOutputBuilderFactory(TestDriverObservedOutputBuilder builder) {
        this.builder = builder;
        builder.instance = null;
    }

    private TestDriverObservedOutputBuilder create(String sourceType, String source) {
        builder.instance = new TestDriverObservedOutput();
        builder.instance.setSourceType(sourceType);
        builder.instance.setSource(source);
        return builder;
    }

    public TestDriverObservedOutputBuilder error(String source, Throwable throwable) {
        logger.error(throwable.getMessage());
        return create(ERROR_TYPE, source).setOutput(throwable.getMessage());
    }

    public TestDriverObservedOutputBuilder success(String source, String command) {
        return create(SUCCESS_TYPE, source).setOutput(command);
    }

    public TestDriverObservedOutputBuilder log(String source, String message) {
        logger.info(message);
        return create(LOG_TYPE, source).setOutput(message);
    }

    public TestDriverObservedOutputBuilder change(String source, String status) {
        logger.warn(status);
        return create(CHANGE_TYPE, source).setOutput(status);
    }

    public TestDriverObservedOutputBuilder getBuilder() {
        return builder;
    }
}
