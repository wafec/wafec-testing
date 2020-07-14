package wafec.testing.execution.robustness;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@ToString
public abstract class DataMutationOperator {
    @Getter
    protected String name;
    @Getter
    protected String dataType;
    @Getter
    protected String category;

    public abstract String mutateValue(String value) throws CouldNotApplyOperatorException;

    protected void throwIfNull(Object value) throws CouldNotApplyOperatorException {
        if (value == null)
            throw new CouldNotApplyOperatorException("Could not apply to null");
    }

    protected void throwIfEmpty(String value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        if (StringUtils.isEmpty(value))
            throw new CouldNotApplyOperatorException("Could not apply to empty string");
    }

    protected void throwIfEmpty(List value) throws CouldNotApplyOperatorException {
        throwIfNull(value);
        if (value.size() == 0)
            throw new CouldNotApplyOperatorException("Could not apply to empty list");
    }
}
