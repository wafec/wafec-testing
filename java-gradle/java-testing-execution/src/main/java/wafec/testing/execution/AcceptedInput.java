package wafec.testing.execution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Optional;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AcceptedInput implements InputSignature {
    private String signature;

    @Override
    public boolean match(InputSignature otherSignature) {
        return Optional.ofNullable(otherSignature).map(InputSignature::getSignature)
                .equals(Optional.ofNullable(signature));
    }
}
