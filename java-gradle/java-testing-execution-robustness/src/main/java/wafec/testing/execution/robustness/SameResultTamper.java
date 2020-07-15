package wafec.testing.execution.robustness;

import org.springframework.stereotype.Component;

@Component
public class SameResultTamper implements DataTamper {
    @Override
    public Object adulterate(Object data, String sourceKey, String context,
                             RobustnessTestExecution robustnessTestExecution) {
        return data;
    }
}
