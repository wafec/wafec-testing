package wafec.testing.execution;

public interface InputSignature {
    String getSignature();
    boolean match(InputSignature otherSignature);
}
