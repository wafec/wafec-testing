package wafec.testing.execution;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseManagedStateRepository extends CrudRepository<TestCaseManagedState, Long> {
    @Query("SELECT m FROM TestCaseManagedState m, TestExecution e WHERE e = ?1 AND e.testCase = m.testCase")
    List<TestCaseManagedState> findByTestExecution(TestExecution testExecution);
    @Query("SELECT m FROM TestCaseManagedState m WHERE m.testCase = ?1 AND m.name = ?2")
    TestCaseManagedState findByTestCaseAndState(TestCase testCase, String state);
    @Query("SELECT m FROM TestCaseManagedState m WHERE m.testCase = ?1")
    List<TestCaseManagedState> findByTestCase(TestCase testCase);
}
