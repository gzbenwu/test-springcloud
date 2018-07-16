package test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;

//@Profile只能与本地的spring.profiles.active配合使用，不与configserver的spring.cloud.config.profile相通
@Profile({ "!t1", "t3" })
@Configuration
public class UserIDAuditorBean implements AuditorAware<String> {
	@Override
	public String getCurrentAuditor() {
		return System.currentTimeMillis() + "";
	}
}
