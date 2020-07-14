package wafec.testing.execution.robustness;

public class DataParseException extends DataInterceptionException {
    public DataParseException() {
        super();
    }

    public DataParseException(String message) {
        super(message);
    }

    public DataParseException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
