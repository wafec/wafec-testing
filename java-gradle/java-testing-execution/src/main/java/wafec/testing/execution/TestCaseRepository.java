package wafec.testing.execution;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends CrudRepository<TestCase, Long> {
    List<TestCase> findByTargetSystem(String targetSystem);
}
