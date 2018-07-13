package test.entity.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TestValidator implements ConstraintValidator<TestValidatorAnnotation, String> {

	@Override
	public void initialize(TestValidatorAnnotation constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.trim().length() < 10) {
			return false;
		}
		return true;
	}
}