package test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpServletRequest res = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		request.getHeaders().add("AuthToken", res.getLocalAddr() + "***" + res.getHeader("AuTk"));
		return execution.execute(request, body);
	}
}