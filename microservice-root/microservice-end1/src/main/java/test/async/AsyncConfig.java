package test.async;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
	// when uses @Component to activate async, no need a bean name here, just use @Async instead of @Async("***")
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(3);
		executor.setQueueCapacity(5);
		executor.setThreadNamePrefix("MainAsyncExecutor-");
		executor.setTaskDecorator(new ParentThreadNameDecorator());
		executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			public void rejectedExecution(Runnable arg0, ThreadPoolExecutor arg1) {
				System.out.println(".........[" + arg0 + "]......... One Async Execute Action Rejected! QueueCount:" + arg1.getQueue().size() + " ActiveCount:" + arg1.getActiveCount() + " ExecutedCount: " + arg1.getTaskCount());
			}
		});
		executor.initialize();
		return executor;
	}

	@Bean
	public Executor slaveAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(1);
		executor.setQueueCapacity(10);
		executor.setThreadNamePrefix("SlaveAsyncExecutor-");
		executor.initialize();
		return executor;
	}

	// for no return value method calls only, because there is not a Future obj can be called and catch the exception
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			public void handleUncaughtException(Throwable arg0, Method arg1, Object... arg2) {
				new Exception("Method:" + arg1.getName() + " Param1:" + arg2[0], arg0).printStackTrace();
			}
		};
	}
}
