package wafec.testing.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wafec.testing.core.MapGet;

public abstract class AbstractTestDriverMonitor implements TestDriverMonitor {
    public static final String SOURCE = AbstractTestDriverMonitor.class.getName();

    protected abstract TestDriverMonitorResult monitorInternal(MapGet mapGet, TestDriverObservedOutputBuilder builder) throws
            Exception;

    Logger logger = LoggerFactory.getLogger(AbstractTestDriverMonitor.class);

    @Override
    public TestDriverMonitorResult monitor(MapGet mapGet) {
        var builder = new TestDriverObservedOutputBuilder();
        try {
            return monitorInternal(mapGet, builder);
        } catch (Exception exc) {
            builder.and().error(SOURCE, exc);
            return new TestDriverMonitorResult(false, builder.buildList());
        }
    }
}
