package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;

import test.entity.User;

//@Profile只能与本地的spring.profiles.active配合使用，不与configserver的spring.cloud.config.profile相通
@Profile({ "!t2", "t3" })
@Configuration
public class UserIDAuditorBean implements AuditorAware<User> {
	Logger logger = LoggerFactory.getLogger(UserIDAuditorBean.class);

	@PostConstruct
	public void init() {
		logger.info(">>>>>>>>> UserIDAuditorBean init ...");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				logger.info(">>>>>>>>> Shutdown invoked ...");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
				logger.info(">>>>>>>>> Exit ...");
			}
		});
	}

	@PreDestroy
	public void close() {
		logger.info(">>>>>>>>> UserIDAuditorBean destroy ...");
	}

	@Override
	public User getCurrentAuditor() {
		// 由于是外连表，所以关于这个表的操作需要在这里先预处理，spring不会自动为User表插入记录。例如，先查询是否已存在该用户，否则先创建插入
		User u = new User();
		u.setId("" + Thread.currentThread().getId());
		u.setName(Thread.currentThread().getName());
		return u;
	}
}
