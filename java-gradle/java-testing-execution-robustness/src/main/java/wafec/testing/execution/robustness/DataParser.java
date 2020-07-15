package wafec.testing.execution.robustness;

public interface DataParser {
    ApplicationData handle(byte[] data) throws DataParseException;
}
