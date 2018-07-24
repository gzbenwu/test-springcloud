package test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Configuration
public class RequestURIInterceptor extends WebMvcConfigurerAdapter {
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogHandlerInterceptor());
	}
}

class LogHandlerInterceptor extends HandlerInterceptorAdapter {
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		MarkUriFilter.removeRequestUri();
	}
}
