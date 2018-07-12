package test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ServiceLinkClient serviceLinkClient;

	@RequestMapping(value = "/headerPass", method = { RequestMethod.GET })
	public Map<String, Object> headerPass(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> hns = req.getHeaderNames();
		while (hns.hasMoreElements()) {
			String header = hns.nextElement();
			map.put(header, req.getHeader(header));
		}
		return map;
	}

	@RequestMapping(value = "/serverLink", method = { RequestMethod.GET })
	public Map<String, Object> getServerLink(HttpServletRequest req) {
		@SuppressWarnings("unchecked")
		Map<String, Object> sub = restTemplate.getForObject("http://microservice-middle1/serverLink", Map.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("port", port);
		map.put("appConfigKey", appConfigKey);
		map.put("linkTo", sub);
		Map<String, Object> headers = new HashMap<String, Object>();
		Enumeration<String> hns = req.getHeaderNames();
		while (hns.hasMoreElements()) {
			String header = hns.nextElement();
			headers.put(header, req.getHeader(header));
		}
		map.put("RequestHeaders_infront", headers);
		return map;
	}

	@RequestMapping(value = "/feign", method = { RequestMethod.GET })
	public Map<String, Map<String, Object>> feign(HttpServletRequest req) {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("timestamp", System.currentTimeMillis() + "");
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		map.put("get", serviceLinkClient.getParam("P1", "TestHeader1"));
		map.put("post", serviceLinkClient.setParam("P2", "Valueeeeee", body));
		Map<String, Object> headers = new HashMap<String, Object>();
		Enumeration<String> hns = req.getHeaderNames();
		while (hns.hasMoreElements()) {
			String header = hns.nextElement();
			headers.put(header, req.getHeader(header));
		}
		map.put("RequestHeaders_infront", headers);
		return map;
	}
}
