package test;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeginInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		HttpServletRequest res = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		requestTemplate.header("AuthToken", "***" + res.getHeader("AuTk"));
	}
}