package wafec.testing.execution;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TEST_OUTPUT")
public class TestOutput {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @Column(name = "created_at")
    private Date createdAt;
    @Lob
    @Column(name = "output", length = 50000)
    private String output;
    private String source;
    @Column(name = "source_type")
    private String sourceType;
    private int position;
}
