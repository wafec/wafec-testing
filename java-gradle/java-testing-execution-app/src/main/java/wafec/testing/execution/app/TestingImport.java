package wafec.testing.execution.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;
import wafec.testing.execution.*;
import wafec.testing.execution.app.models.TestArgumentItem;
import wafec.testing.execution.app.models.TestCaseImportModel;
import wafec.testing.execution.app.models.TestCaseItem;
import wafec.testing.execution.app.models.TestInputItem;
import wafec.testing.execution.app.services.TestImportService;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Callable;

@Command(name = "import")
@Component
public class TestingImport implements Callable<Integer> {
    @Option(names = { "-r", "--replace" }, defaultValue = "false", negatable = true)
    private boolean replace;
    @Parameters(paramLabel = "FILE")
    private File[] files;
    @Autowired
    private TestImportService testImportService;

    @Override
    public Integer call() throws Exception {
        for (File file : files)
            testImportService.importTest(file, replace);
        return 0;
    }


}
