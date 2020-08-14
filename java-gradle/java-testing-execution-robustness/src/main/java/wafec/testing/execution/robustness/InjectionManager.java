package wafec.testing.execution.robustness;

public interface InjectionManager {
    void handleInjectionManaged(Object data, InjectionTarget injectionTarget);
    Object inject(Object data, InjectionFault injectionFault);
}
