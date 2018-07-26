package test.async;

import org.springframework.core.task.TaskDecorator;

public class ParentThreadNameDecorator implements TaskDecorator {
	private static final ThreadLocal<String> parentThreadName = new ThreadLocal<String>();

	@Override
	public Runnable decorate(Runnable runnable) {
		final String currentThreadName = Thread.currentThread().getName();
		return () -> {
			parentThreadName.set(currentThreadName);
			runnable.run();
			parentThreadName.get();
		};
	}

	public static String getParentThreadName() {
		return parentThreadName.get();
	}
}
