package wafec.testing.execution.app;

import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Command(name = "testing", subcommands = {
        TestingImport.class,
        TestingExecution.class,
        TestingClean.class
})
@Component
public class Testing {
}
