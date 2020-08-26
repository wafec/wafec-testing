package wafec.testing.execution;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TestExecutionObservedOutputRepository extends CrudRepository<TestExecutionObservedOutput, Long> {
    @Query("SELECT o FROM TestExecutionObservedOutput o, TestExecution e WHERE o.testExecution = e AND e.testCase = ?1")
    List<TestExecutionObservedOutput> findByTestCase(TestCase testCase);
    @Query("SELECT max(to.position) FROM TestExecutionObservedOutput teo, TestOutput to WHERE teo.testOutput = to AND teo.testExecution = ?1 AND teo.testInput = ?2")
    int maxPositionByTestExecutionAndTestInput(TestExecution testExecution, TestInput testInput);
    @Query("SELECT o FROM TestExecutionObservedOutput o LEFT JOIN FETCH o.testOutput to WHERE o.testExecution = ?1 ORDER BY to.createdAt")
    List<TestExecutionObservedOutput> findByTestExecution(TestExecution testExecution);
    @Query("SELECT o FROM TestExecutionObservedOutput o LEFT JOIN FETCH o.testOutput to WHERE o.testExecution = :testExecution AND to.createdAt BETWEEN :startTime AND :endTime AND (to.sourceType LIKE '%sch%') ORDER BY to.createdAt")
    List<TestExecutionObservedOutput> findByTestExecutionAndSchLogsBetweenSpecificDate(@Param("testExecution") TestExecution testExecution,
                                                                                       @Param("startTime") Date startTime,
                                                                                       @Param("endTime") Date endTime);
}
