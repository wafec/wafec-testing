package wafec.testing.execution;

import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestDriverInputModelBuilder {
    private final Map<String, TestDriverInputFunction> signatureMappings = new HashMap<>();

    public TestDriverInputModelBuilder map(String signature, TestDriverInputFunction function) {
        signatureMappings.put(Optional.of(signature).get(), function);
        return this;
    }

    public TestDriverInputFunction get(String signature) throws TestDriverInputNotFoundException {
        if (hasSignature(signature))
            return signatureMappings.get(signature);
        throw new TestDriverInputNotFoundException(signature);
    }

    public TestDriverInputFunction get(InputSignature inputSignature) throws TestDriverInputNotFoundException {
        return get(inputSignature.getSignature());
    }

    public boolean hasSignature(String signature) {
        return signatureMappings.containsKey(signature);
    }

    public boolean hasSignature(InputSignature inputSignature) {
        return hasSignature(inputSignature.getSignature());
    }

    public List<String> buildSignatureList() {
        return new ArrayList<>(signatureMappings.keySet());
    }
}
