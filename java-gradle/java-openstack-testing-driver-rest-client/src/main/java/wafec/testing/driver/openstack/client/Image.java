package wafec.testing.driver.openstack.client;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Image {
    private String id;
    @NonNull private String name;
    @SerializedName("container_format")
    @NonNull private String containerFormat;
    @SerializedName("disk_format")
    @NonNull private String diskFormat;
    @NonNull private String data;
}
