package test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(value = "custom.test")
@PropertySource("local.properties")
public class ConfigrationPropertySource {
	@Value("${local.application}")
	private String application;

	private String appConfigKey;

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getAppConfigKey() {
		return appConfigKey;
	}

	public void setAppConfigKey(String appConfigKey) {
		this.appConfigKey = appConfigKey;
	}
}
