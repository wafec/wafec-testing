package wafec.testing.execution.analysis;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "EVALUATION_TEST_EXECUTION_STATUS")
@Data
public class EvaluationTestExecutionStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;
    @OneToOne(mappedBy = "evaluationTestExecutionStatus")
    private EvaluationTestExecution evaluationTestExecution;
    @Column(name = "status_type")
    @Enumerated
    private EvaluationTestExecutionStatusTypes evaluationTestExecutionStatusType;
    @Lob
    @Column(name = "description", length = 56000)
    private String description;

    public static EvaluationTestExecutionStatus of(EvaluationTestExecution evaluationTestExecution,
                                                   EvaluationTestExecutionStatusTypes evaluationTestExecutionStatusType,
                                                   String description) {
        var instance = new EvaluationTestExecutionStatus();
        instance.setEvaluationTestExecution(evaluationTestExecution);
        instance.setEvaluationTestExecutionStatusType(evaluationTestExecutionStatusType);
        instance.setDescription(description);
        return instance;
    }
}
