package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TestDataValueNotFoundException extends Exception {
    @Getter
    private String name;
}
