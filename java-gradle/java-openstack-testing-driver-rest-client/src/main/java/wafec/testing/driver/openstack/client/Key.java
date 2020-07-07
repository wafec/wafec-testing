package wafec.testing.driver.openstack.client;

import lombok.*;

import javax.persistence.*;
import java.util.Base64;

@Data
@ToString
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@Entity
@Table(name = "PASS_KEY")
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "assigned")
    @Column(name = "id", insertable = true, updatable = true, unique = true, nullable = false)
    private long id;
    @Column(name = "auth_url")
    @NonNull private String authUrl;
    @NonNull private String username;
    @Column(name = "passwd")
    @NonNull private String password;
    @Column(name = "user_domain_name")
    @NonNull private String userDomainName;
    @Column(name = "project_domain_name")
    @NonNull private String projectDomainName;
    @Column(name = "project_name")
    @NonNull private String projectName;
}
