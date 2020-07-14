package wafec.testing.execution.robustness;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ApplicationData {
    private String name = "";
    private String context= "";
    private String value = "";
    private String dataType = "";
    private byte[] raw;
    private final List<ApplicationData> nestedData = new ArrayList<>();

    public boolean compareChange(ApplicationData other) {
        var otherOpt = Optional.of(other);
        if (!otherOpt.map(ApplicationData::getName).equals(Optional.of(name)) ||
            !otherOpt.map(ApplicationData::getContext).equals(Optional.of(context)) ||
            !otherOpt.map(ApplicationData::getDataType).equals(Optional.of(dataType)))
            throw new IllegalArgumentException("Can not compare a change of different context");
        return Optional.of(other).map(ApplicationData::getValue).equals(Optional.of(value));
    }

    public void insert(int index, ApplicationData data) {
        nestedData.add(index, data);
    }

    public void add(ApplicationData data) {
        nestedData.add(data);
    }

    public void remove(int index) {
        nestedData.remove(index);
    }

    public int indexOf(ApplicationData data) {
        return nestedData.indexOf(data);
    }

    public static ApplicationData ofRaw(byte[] raw) {
        var result = new ApplicationData();
        result.setRaw(raw);
        return result;
    }
}
