package wafec.testing.core;

public enum CommonTypes {
    STRING("string"),
    NUMBER("number"),
    LIST("list"),
    MAP("map"),
    OBJECT("object"),
    NULL("null"),
    UNKNOWN("unknown"),
    BOOLEAN("boolean");

    private String _type;

    private CommonTypes(String type) {
        _type = type;
    }

    @Override
    public String toString() {
        return _type;
    }
}
