package wafec.testing.support.rabbitmq.management;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ExchangeView {
    private String name;
    @SerializedName("type")
    private String exchangeType;
    private boolean durable;
    @SerializedName("auto_delete")
    private boolean autoDelete;
    private boolean internal;
}
