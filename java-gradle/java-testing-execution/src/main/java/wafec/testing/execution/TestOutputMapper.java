package wafec.testing.execution;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TestOutputMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "position", ignore = true)
    TestOutput fromTestDriverObservedOutput(TestDriverObservedOutput testDriverObservedOutput);
}
