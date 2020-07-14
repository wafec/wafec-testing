package wafec.testing.execution.robustness;

import org.springframework.stereotype.Component;

@Component
public class DummyDataCorruption implements DataCorruption {
    @Override
    public String corrupt(String value, String valueType) throws CouldNotApplyOperatorException {
        return value;
    }

    @Override
    public ApplicationData corrupt(ApplicationData applicationData) throws CouldNotApplyOperatorException {
        return applicationData;
    }
}
