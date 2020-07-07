package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class PreConditionViolationException extends Exception {
    @Getter
    private String rule;
}
