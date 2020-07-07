package wafec.testing.execution;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TEST_OUTPUT")
public class TestOutput {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "created_at")
    private Date createdAt;
    private String output;
    private String source;
    @Column(name = "source_type")
    private String sourceType;
    private int position;
}
