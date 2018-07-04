package test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Controller {

	@Value("${custom.test.appConfigKey}")
	private String appConfigKey;

	@Value("${server.port}")
	private String port;

	@Value("${custom.testpw}")
	private String testpw;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@RequestMapping(value = "/headerPass", method = { RequestMethod.GET })
	public Map<String, Object> headerPass(HttpServletRequest req) {
		Map<String, Object> main = new HashMap<String, Object>();
		Map<String, Object> front1 = restTemplate.getForObject("http://zuul-server/front1/headerPass", Map.class);
		main.put("front1Server", front1);
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> hns = req.getHeaderNames();
		while (hns.hasMoreElements()) {
			String header = hns.nextElement();
			map.put(header, req.getHeader(header));
		}
		main.put("middle1Server", map);
		return main;
	}

	@RequestMapping(value = "/serverLink", method = { RequestMethod.GET })
	public Map<String, Object> getServerLink(HttpServletRequest req) {
		Map<String, Object> end1 = restTemplate.getForObject("http://microservice-end1/serverLink", Map.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("port", port);
		map.put("appConfigKey", appConfigKey);
		map.put("linkTo", end1);
		map.put("testpw", testpw);
		ServiceInstance serviceInstance = loadBalancerClient.choose("microservice-end1");
		map.put("chooseServiceMeta", serviceInstance.getMetadata());
		map.put("chooseServicePort", serviceInstance.getPort());
		Map<String, Object> headers = new HashMap<String, Object>();
		Enumeration<String> hns = req.getHeaderNames();
		while (hns.hasMoreElements()) {
			String header = hns.nextElement();
			headers.put(header, req.getHeader(header));
		}
		map.put("RequestHeaders_inmidd", headers);
		return map;
	}

	@RequestMapping(value = "/param/{paramName}/get", method = { RequestMethod.GET })
	public Map<String, Object> getParam(@PathVariable("paramName") String paramName, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("paramName", paramName);
		Map<String, Object> headers = new HashMap<String, Object>();
		Enumeration<String> hns = req.getHeaderNames();
		while (hns.hasMoreElements()) {
			String header = hns.nextElement();
			headers.put(header, req.getHeader(header));
		}
		map.put("RequestHeaders_inmidd", headers);
		return map;
	}

	@RequestMapping(value = "/param/{paramName}/post/{paramValue}", method = { RequestMethod.POST })
	public Map<String, Object> setParam(@PathVariable("paramName") String paramName, @PathVariable("paramValue") String paramValue, @RequestBody Map<String, Object> body, HttpServletRequest req) {
		body.put("paramName", paramName);
		body.put("paramValue", paramValue);
		Map<String, Object> headers = new HashMap<String, Object>();
		Enumeration<String> hns = req.getHeaderNames();
		while (hns.hasMoreElements()) {
			String header = hns.nextElement();
			headers.put(header, req.getHeader(header));
		}
		body.put("RequestHeaders_inmidd", headers);
		return body;
	}
}
