package wafec.testing.execution;

public interface EnvironmentController {
    void setUp() throws EnvironmentException;
    void tearDown();
}
