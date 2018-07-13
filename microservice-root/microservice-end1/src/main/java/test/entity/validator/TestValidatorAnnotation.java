package test.entity.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = TestValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestValidatorAnnotation {
	static final String defMsg = "My_Validation_Failed";

	String message() default defMsg;

	String code1() default "0000A";

	String code2() default "0000B";

	String code3() default "0000C";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}