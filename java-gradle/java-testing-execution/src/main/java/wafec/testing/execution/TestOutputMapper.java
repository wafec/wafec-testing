package wafec.testing.execution;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestOutputMapper {
    TestOutput fromTestDriverObservedOutput(TestDriverObservedOutput testDriverObservedOutput);
}
