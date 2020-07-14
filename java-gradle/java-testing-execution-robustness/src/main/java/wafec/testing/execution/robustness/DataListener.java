package wafec.testing.execution.robustness;

public interface DataListener {
    DataOperation intercept(byte[] raw) throws DataInterceptionException;
}
