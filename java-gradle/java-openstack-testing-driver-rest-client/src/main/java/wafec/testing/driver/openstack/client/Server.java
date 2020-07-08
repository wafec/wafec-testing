package wafec.testing.driver.openstack.client;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Server {
    private String id;
    private String name;
    private String flavor;
    private String image;
    private String network;
    private String status;
    @SerializedName("power_state")
    private String powerState;
    @SerializedName("task_state")
    private String taskState;
    @SerializedName("vm_state")
    private String vmState;
}
