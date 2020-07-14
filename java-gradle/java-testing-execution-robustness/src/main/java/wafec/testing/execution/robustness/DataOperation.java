package wafec.testing.execution.robustness;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class DataOperation {
    public static final String ERROR = "error";
    public static final String CANNOT_APPLY = "cannot_apply";
    public static final String MUTATED = "mutated";
    public static final String IGNORED = "decide_skip";

    private Date realizedAt;
    private ApplicationData previous;
    private ApplicationData current;
    private String description;
}
