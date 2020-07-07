package wafec.testing.driver.openstack.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
public class Volume {
    private String id;
    private String name;
    private String availabilityZone;
    private int size;
}
