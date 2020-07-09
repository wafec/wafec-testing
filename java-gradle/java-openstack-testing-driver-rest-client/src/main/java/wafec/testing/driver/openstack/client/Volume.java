package wafec.testing.driver.openstack.client;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Data
@NoArgsConstructor
public class Volume {
    private String id;
    private String name;
    @SerializedName("availability_zone")
    private String availabilityZone;
    private String size;
    private String status;
    private List<Attachment> attachments;
}
