package test.aop;

import org.springframework.stereotype.Component;

@Component
public class TestService {
	public StringBuilder doSomething(String arg0, int arg1) throws Exception {
		System.out.println("do......." + arg0 + "-" + arg1);
		if (arg1 > 3) {
			throw new Exception("!!!!!!!!!");
		}
		return new StringBuilder("done");
	}
}
