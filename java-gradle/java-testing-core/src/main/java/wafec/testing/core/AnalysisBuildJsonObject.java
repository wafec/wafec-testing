package wafec.testing.core;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ANALYSIS_BUILD_JSON_OBJECT")
@Data
public class AnalysisBuildJsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @Column(name = "before_data", length = 500000)
    private String beforeData;
    @Column(name = "after_data", length = 500000)
    private String afterData;
    @Column(name = "similarity_score")
    private double similarityScore;
    @Column(name = "similarity_algorithm")
    private String similarityAlgorithm;
    @Column(name = "created_at")
    private Date createdAt;
}
