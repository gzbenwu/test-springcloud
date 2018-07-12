package test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@SpringCloudApplication
@EnableFeignClients
public class Front1Service {
	public static void main(String[] args) {
		SpringApplication.run(Front1Service.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateInterceptor restTemplateInterceptor) {
		RestTemplate restTemplate = new RestTemplate();
		List<ClientHttpRequestInterceptor> list = restTemplate.getInterceptors();
		if (list == null) {
			list = new ArrayList<ClientHttpRequestInterceptor>();
			restTemplate.setInterceptors(list);
		}
		list.add(restTemplateInterceptor);
		return restTemplate;
	}
}
