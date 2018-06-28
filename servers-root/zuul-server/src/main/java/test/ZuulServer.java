package test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
//import org.springframework.cloud.netflix.zuul.EnableZuulServer;

import com.netflix.zuul.ZuulFilter;

// @EnableZuulServer
@SpringCloudApplication
@EnableZuulProxy
public class ZuulServer {
	public static void main(String[] args) {
		SpringApplication.run(ZuulServer.class, args);
	}

	@Bean
	public ZuulFilter filter(@Value("${custom.serverid}") String id, @Value("${custom.zuul.shouldFilter:true}") String shouldFilter) {
		return new CustomZuulFilter(id, shouldFilter);
	}
}
