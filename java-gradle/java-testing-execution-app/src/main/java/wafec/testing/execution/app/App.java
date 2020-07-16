package wafec.testing.execution.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;
import wafec.testing.execution.app.commandline.Testing;

@SpringBootApplication
public class App implements CommandLineRunner, ExitCodeGenerator {
    private Testing testing;
    private final IFactory factory;

    private int exitCode;

    public App(Testing testing, IFactory factory) {
        this.testing = testing;
        this.factory = factory;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CommandLine commandLine = new CommandLine(testing, factory);
        exitCode = commandLine.execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
