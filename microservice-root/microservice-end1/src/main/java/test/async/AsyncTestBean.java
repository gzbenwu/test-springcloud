package test.async;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTestBean {

	@Async
	public CompletableFuture<String> run1(String p1) throws InterruptedException {
		String uuid = UUID.randomUUID().toString();
		System.out.println("P1-Wait(" + uuid + ")-------------->[" + ParentThreadNameDecorator.getParentThreadName() + ":" + Thread.currentThread().getName() + "] P:" + p1);
		Thread.sleep(2000);
		System.out.println("P1-Finish(" + uuid + ")-------------->[" + ParentThreadNameDecorator.getParentThreadName() + ":" + Thread.currentThread().getName() + "] P:" + p1);
		return CompletableFuture.completedFuture("done");
	}

	@Async("slaveAsyncExecutor")
	public Future<String> run2(String p1) throws InterruptedException {
		String uuid = UUID.randomUUID().toString();
		System.out.println("P2-Wait(" + uuid + ")-------------->[" + ParentThreadNameDecorator.getParentThreadName() + ":" + Thread.currentThread().getName() + "] P:" + p1);
		Thread.sleep(1000);
		throw new RuntimeException("!!!!!!");
	}

	@Async("slaveAsyncExecutor")
	public void run3(String p1) {
		String uuid = UUID.randomUUID().toString();
		System.out.println("P3-Wait(" + uuid + ")-------------->[" + ParentThreadNameDecorator.getParentThreadName() + ":" + Thread.currentThread().getName() + "] P:" + p1);
		throw new RuntimeException("++++++++++");
	}
}
