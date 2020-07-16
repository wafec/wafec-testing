package wafec.testing.support.rabbitmq;

public class DataTransformerException extends Exception {
    public DataTransformerException() {
        super();
    }

    public DataTransformerException(String message) {
        super(message);
    }

    public DataTransformerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
