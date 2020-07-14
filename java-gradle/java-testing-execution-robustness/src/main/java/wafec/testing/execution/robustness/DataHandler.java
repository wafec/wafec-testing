package wafec.testing.execution.robustness;

public interface DataHandler {
    ApplicationData handle(byte[] raw) throws DataParseException;
}
