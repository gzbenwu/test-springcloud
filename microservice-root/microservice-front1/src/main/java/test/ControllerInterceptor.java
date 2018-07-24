package test;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Configuration
public class ControllerInterceptor extends WebMvcConfigurerAdapter {
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogHandlerInterceptor());
	}
}

class LogHandlerInterceptor extends HandlerInterceptorAdapter {
	private static final ThreadLocal<String> uuid = new ThreadLocal<String>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println("--------------Pre:" + uuid.get());
		uuid.set(UUID.randomUUID().toString());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Request[" + request.getRequestURI() + "?" + request.getQueryString() + "]" + uuid.get() + " Auth:" + auth);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Handled[" + request.getRequestURI() + "?" + request.getQueryString() + "]" + uuid.get() + " Auth:" + auth);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("Completed[" + request.getRequestURI() + "?" + request.getQueryString() + "]" + uuid.get() + " Auth:" + auth);
	}
}
