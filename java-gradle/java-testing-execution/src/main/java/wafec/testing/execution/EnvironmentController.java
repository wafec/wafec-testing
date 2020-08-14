package wafec.testing.execution;

import java.util.List;

public interface EnvironmentController {
    void setUp() throws EnvironmentException;
    void tearDown();
    List<String> getNodeNames() throws EnvironmentException;
    List<String> getServiceProcessNames(String nodeName) throws EnvironmentException;
    void shutdownNode(String nodeName) throws EnvironmentException;
    void stopServiceProcess(String nodeName, String serviceProcessName) throws EnvironmentException;
}
