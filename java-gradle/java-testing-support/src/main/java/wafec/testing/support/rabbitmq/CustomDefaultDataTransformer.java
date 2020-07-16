package wafec.testing.support.rabbitmq;

import java.util.function.Function;

public interface CustomDefaultDataTransformer {
    byte[] apply(byte[] entry) throws DataTransformerException;
}
