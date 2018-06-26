package test;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
//import org.springframework.cloud.netflix.zuul.EnableZuulServer;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

// @EnableZuulServer
@SpringCloudApplication
@EnableZuulProxy
public class ZuulClient {
	public static void main(String[] args) {
		SpringApplication.run(ZuulClient.class, args);
	}

	@Bean
	public ZuulFilter filter(@Value("${custom.zuul.shouldFilter:true}") String shouldFilter) {
		return new Filter(shouldFilter);
	}

	class Filter extends ZuulFilter {
		private String shouldFilter;

		public Filter(String shouldFilter) {
			this.shouldFilter = shouldFilter;
		}

		/**
		 * 过滤器类型 pre 事前 routing 路由请求时候调用 error 发生错误时候调用
		 * 
		 * @return
		 */
		@Override
		public String filterType() {
			return "pre";
		}

		@Override
		public int filterOrder() {
			return 5;
		}

		/**
		 * 是否过来 0 不过滤 1 过滤
		 * 
		 * @return
		 */
		@Override
		public boolean shouldFilter() {
			return "true".equals(shouldFilter);
		}

		/**
		 * 拦截的具体操作 验证token
		 * 
		 * @return
		 */
		@Override
		public Object run() {
			RequestContext ctx = RequestContext.getCurrentContext();
			HttpServletRequest request = ctx.getRequest();
			System.out.println("----------zuul-client request: " + request.getRequestURI());
			return null;
		}
	}
}
