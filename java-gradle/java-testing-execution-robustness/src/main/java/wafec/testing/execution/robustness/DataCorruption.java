package wafec.testing.execution.robustness;

public interface DataCorruption {
    String STRING = "string";
    String NUMBER = "number";
    String BOOLEAN = "boolean";
    String OBJECT = "object";
    String LIST = "collection";
    String MAP = "dictionary";

    String corrupt(String value, String valueType) throws CouldNotApplyOperatorException;
    ApplicationData corrupt(ApplicationData applicationData) throws CouldNotApplyOperatorException;
}
