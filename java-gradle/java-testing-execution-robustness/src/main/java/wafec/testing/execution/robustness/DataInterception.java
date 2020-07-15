package wafec.testing.execution.robustness;

public interface DataInterception {
    void turnOn(DataListener dataListener) throws DataInterceptionException;
    void turnOff() throws DataInterceptionException;
}
