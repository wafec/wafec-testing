package wafec.testing.execution.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor(staticName = "of")
public class SchOutput {
    private String line;
    private Date parsedAt;
    private String source;
}
