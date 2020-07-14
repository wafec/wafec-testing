package wafec.testing.execution.robustness;

public interface DataInterception {
    void turnOn(DataListener dataCallback) throws DataInterceptionException;
    void turnOff() throws DataInterceptionException;
}
