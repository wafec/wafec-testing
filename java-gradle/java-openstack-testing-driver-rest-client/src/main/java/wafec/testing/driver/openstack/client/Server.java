package wafec.testing.driver.openstack.client;

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
}
