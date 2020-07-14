package wafec.testing.execution.robustness;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class JsonBaseOperator<T> extends DataMutationOperator {

    private T parse(String value) throws CouldNotApplyOperatorException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<T> typeReference = new TypeReference<T>() { };
            return mapper.readValue(value, typeReference);
        } catch (IOException exc) {
            throw new CouldNotApplyOperatorException(exc.getMessage(), exc);
        }
    }

    private String unparse(T value) throws CouldNotApplyOperatorException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (IOException exc) {
            throw new CouldNotApplyOperatorException(exc.getMessage(), exc);
        }
    }

    protected abstract T mutateObject(T value) throws CouldNotApplyOperatorException;

    @Override
    public String mutateValue(String value) throws CouldNotApplyOperatorException {
        T valueObject = parse(value);
        valueObject = mutateObject(valueObject);
        return unparse(valueObject);
    }
}
