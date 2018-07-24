package test.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Aop {
	private int a;

	// 1
	@Before(value = "execution(public StringBuilder test.aop.TestService.doSomething(String,int)) && args(arg0,arg1)")
	public void before(JoinPoint point, String arg0, int arg1) {
		System.out.println("before......." + arg0 + "-" + arg1 + "-" + point.getTarget() + "-" + point.getArgs()[0]);
	}

	// 2, no matter there is exception or not
	@After(value = "execution(public StringBuilder test.aop.TestService.doSomething(String,int)) && args(arg0,arg1)")
	public void after(JoinPoint point, String arg0, int arg1) {
		System.out.println("after......." + point.getTarget() + "-" + point.getArgs()[0]);
	}

	// 3, success
	@AfterReturning(returning = "returnValue", value = "execution(public StringBuilder test.aop.TestService.doSomething(String,int)) && args(arg0,arg1)")
	public void afterReturning(JoinPoint point, StringBuilder returnValue, String arg0, int arg1) {
		returnValue.append("OOOOOOOOOk");
		System.out.println("afterReturning......." + arg0 + "-" + arg1 + "-" + returnValue + "-" + point.getTarget() + "-" + point.getArgs()[1]);
	}

	// 3, error
	@AfterThrowing(throwing = "exception", value = "execution(public StringBuilder test.aop.TestService.doSomething(String,int)) && args(arg0,arg1)")
	public void exception(JoinPoint point, Exception exception, String arg0, int arg1) {
		System.out.println("exception......." + arg0 + "-" + arg1 + "-" + point.getTarget() + "-" + point.getArgs()[0]);
	}

	// 0, all
	@Around(value = "execution(public StringBuilder test.aop.TestService.doSomething(String,int))")
	public StringBuilder exception(ProceedingJoinPoint point) {
		try {
			StringBuilder sb = (StringBuilder) point.proceed(new Object[] { "nothing", this.a++ });
			System.out.println("around......." + sb + "-" + point.getTarget() + "-" + point.getArgs()[1]);
			return new StringBuilder("ok");
		} catch (Throwable e) {
			return new StringBuilder("failed");
			// 如果这里返回null则会触发上面的exception方法
			// return null;
		}
	}
}
