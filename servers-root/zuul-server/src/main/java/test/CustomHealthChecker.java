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
		int s = new Random().nextInt(100);
		switch (s) {
		case 1:
			h = new Health.Builder().down().withDetail("health", "Dead").build();
			break;
		case 2:
			h = new Health.Builder().unknown().withDetail("health", "Not Good").withException(new Exception("E1")).build();
			break;
		case 3:
			h = new Health.Builder().outOfService().withDetail("health", "Bad").withException(new Exception("E2")).build();
			break;
		default:
			h = new Health.Builder().up().withDetail("health", "Good").withDetail("ok?", "yes").build();
			break;
		}
		return h;
	}
}
