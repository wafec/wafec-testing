package wafec.testing.driver.openstack.client;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    @SerializedName("attachment_id")
    private String id;
    @SerializedName("server_id")
    private String server;
    private String device;
    @SerializedName("host_name")
    private String hostname;
}
