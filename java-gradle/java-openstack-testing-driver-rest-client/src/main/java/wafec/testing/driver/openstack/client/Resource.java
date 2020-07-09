package wafec.testing.driver.openstack.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Resource {
    private String id;
}
