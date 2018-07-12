package test;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class CustomZuulFilter extends ZuulFilter {
	private String shouldFilter;
	private String id;

	public CustomZuulFilter(String id, String shouldFilter) {
		this.shouldFilter = shouldFilter;
		this.id = id;
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
		ctx.addZuulRequestHeader("ZuulRequestHeader", "ZuulServerAddedMe");
		ctx.addZuulResponseHeader("ZuulResponseHeader", "ZuulServerAddedMe");
		HttpServletRequest request = ctx.getRequest();
		System.out.println(">>>>>>>>>>>>>>zuul-server-" + id + " request: " + request.getRequestURI());
		return null;
	}
}