package wafec.testing.execution;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreCondition {
    String SECURED = "preConditionSecured";
    String DEFAULT = "preCondition";

    String target() default DEFAULT;
}
