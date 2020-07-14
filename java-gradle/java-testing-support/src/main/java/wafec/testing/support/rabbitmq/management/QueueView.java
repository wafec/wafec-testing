package wafec.testing.support.rabbitmq.management;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueueView {
    private String name;
    @SerializedName("auto_delete")
    private boolean autoDelete;
    private boolean durable;
    private boolean exclusive;
    private String node;
    @SerializedName("vhost")
    private String virtualHost;
    private String state;
    private Integer consumers;
}
