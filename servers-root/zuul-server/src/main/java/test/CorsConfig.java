package test;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig extends WebSecurityConfigurerAdapter {
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		for (String allowedOrigin : "a.com,b.com,c.com".split(",")) {
			corsConfiguration.addAllowedOrigin(allowedOrigin);
		}
		for (String allowedHeader : "X-a,X-b,X-c".split(",")) {
			corsConfiguration.addAllowedHeader(allowedHeader);
		}
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedOrigin("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(source);
	}

	@Override
	public void configure(HttpSecurity httpSecurity) {
		httpSecurity.addFilterBefore(corsFilter(), ChannelProcessingFilter.class);
	}
}