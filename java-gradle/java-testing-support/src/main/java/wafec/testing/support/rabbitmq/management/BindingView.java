package wafec.testing.support.rabbitmq.management;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BindingView {
    private String source;
    private String destination;
    @SerializedName("destination_type")
    private String destinationType;
    @SerializedName("routing_key")
    private String routingKey;
    @SerializedName("vhost")
    private String virtualHost;
}
