package test;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringCloudApplication
// @EnableMongoRepositories
public class End1Service {
	public static void main(String[] args) {
		SpringApplication.run(End1Service.class, args);
	}
}
