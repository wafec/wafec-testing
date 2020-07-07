package wafec.testing.execution;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TEST_CASE")
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @Column(name = "target_system")
    private String targetSystem;
    private String description;
    @Column(name = "created_at")
    private Date createdAt;
}
