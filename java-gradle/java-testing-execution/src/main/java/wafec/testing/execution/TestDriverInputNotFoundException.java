package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TestDriverInputNotFoundException extends Exception {
    @Getter
    private String signature;

    @Override
    public String toString() {
        return String.format("Input of signature '%s' not found", signature);
    }
}
