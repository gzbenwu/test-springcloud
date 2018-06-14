package test;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients
public class Front1Service {
    public static void main(String[] args) {
        SpringApplication.run(Front1Service.class, args);
    }
}
