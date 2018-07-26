package test.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Component("AsyncController")
@RestController
public class Controller {
	private AtomicInteger ai1 = new AtomicInteger();
	private AtomicInteger ai2 = new AtomicInteger();
	private AtomicInteger ai3 = new AtomicInteger();

	@Autowired
	private AsyncTestBean asyncTestBean;

	@RequestMapping(value = "/async1", method = { RequestMethod.GET })
	public String do1() throws Exception {
		CompletableFuture<String> feture = asyncTestBean.run1("" + ai1.incrementAndGet());
		return feture.getNow("NotCompleted");
	}

	@RequestMapping(value = "/async2", method = { RequestMethod.GET })
	public String do2() throws InterruptedException, ExecutionException {
		Future<String> feture = asyncTestBean.run2("" + ai2.incrementAndGet());
		return feture.get();
	}

	@RequestMapping(value = "/async3", method = { RequestMethod.GET })
	public String do3() throws InterruptedException, ExecutionException {
		asyncTestBean.run3("" + ai3.incrementAndGet());
		return "ok";
	}
}
