package wafec.testing.execution.robustness;

public interface DataTamper {
    String STRING = "string";
    String NUMBER = "number";
    String BOOLEAN = "boolean";
    String OBJECT = "object";
    String LIST = "collection";
    String MAP = "dictionary";

    Object adulterate(Object data, String sourceKey, String context, RobustnessTestExecution robustnessTestExecution);
}
