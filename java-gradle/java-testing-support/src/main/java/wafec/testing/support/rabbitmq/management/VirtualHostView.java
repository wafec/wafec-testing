package wafec.testing.support.rabbitmq.management;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VirtualHostView {
    private String name;
}
