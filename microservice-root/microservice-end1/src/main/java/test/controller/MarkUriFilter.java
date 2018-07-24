package test.controller;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class MarkUriFilter implements Filter {
	private static final ThreadLocal<String> requestUri = new ThreadLocal<String>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		requestUri.set(Thread.currentThread().getId() + ":" + URLDecoder.decode(req.getRequestURI() + (req.getQueryString() == null ? "" : "?" + req.getQueryString()), "ISO8859-1"));
		chain.doFilter(request, response);
	}

	public static String getRequestUri() {
		return requestUri.get();
	}

	public static void removeRequestUri() {
		requestUri.remove();
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}
}
