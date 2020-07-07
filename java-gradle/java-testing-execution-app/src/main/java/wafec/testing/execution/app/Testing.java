package wafec.testing.execution.app;

import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

@Command(name = "testing", subcommands = {
        TestingImport.class
})
@Component
public class Testing {
}
