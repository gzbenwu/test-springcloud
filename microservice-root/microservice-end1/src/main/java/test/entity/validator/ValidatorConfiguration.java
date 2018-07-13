package test.entity.validator;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "custom.hibernate_validator_failfast.enable1", havingValue = "true")
@Configuration
public class ValidatorConfiguration {

	@Bean
	@ConditionalOnProperty(name = "custom.hibernate_validator_failfast.enable2", havingValue = "true")
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure().addProperty("hibernate.validator.fail_fast", "true").buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		return validator;
	}
}