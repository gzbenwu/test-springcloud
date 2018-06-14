package test;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;

@SpringCloudApplication
@EnableZuulServer
public class ZuulServer {
    public static void main(String[] args) {
        SpringApplication.run(ZuulServer.class, args);
    }
}
