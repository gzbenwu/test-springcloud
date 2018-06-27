package test;

import java.util.Random;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthChecker implements HealthIndicator {
	@Override
	public Health health() {
		Health h = null;
		int s = new Random().nextInt(4);
		switch (s) {
		case 0:
			h = new Health.Builder().up().withDetail("health", "Good").build();
			break;
		case 1:
			h = new Health.Builder().down().withDetail("health", "Dead").build();
			break;
		case 2:
			h = new Health.Builder().unknown().withDetail("health", "Not Good").withException(new Exception("E1")).build();
			break;
		case 3:
			h = new Health.Builder().outOfService().withDetail("health", "Bad").withException(new Exception("E2")).build();
			break;
		}
		return h;
	}
}
