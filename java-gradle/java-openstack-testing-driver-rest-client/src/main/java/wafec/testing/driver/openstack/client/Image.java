package wafec.testing.driver.openstack.client;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Image {
    private String id;
    @NonNull private String name;
    @NonNull private String containerFormat;
    @NonNull private String diskFormat;
}
