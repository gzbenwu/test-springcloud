package test;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class UserIDAuditorBean implements AuditorAware<String> {
	@Override
	public String getCurrentAuditor() {
		return System.currentTimeMillis() + "";
	}
}
