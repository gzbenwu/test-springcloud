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

	boolean code2() default true;

	short code3() default 9;

	int code4() default 123;

	long code5() default 999;

	double code6() default 0.001;

	float code7() default 1.999F;

	String code8() default "0000B";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}