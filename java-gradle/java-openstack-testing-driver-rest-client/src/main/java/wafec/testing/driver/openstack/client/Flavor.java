package wafec.testing.driver.openstack.client;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class Flavor {
    private String id;
    @NonNull private String name;
    @NonNull private int ram;
    @NonNull  private int vcpus;
    @NonNull private int disk;
}
