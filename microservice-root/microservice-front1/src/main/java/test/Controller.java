package test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

	@RequestMapping(value = "/serverLink", method = { RequestMethod.GET })
	public Map<String, Object> getServerLink() {
		Map<String, String> sub = restTemplate.getForObject("http://microservice-middle1/serverLink", Map.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("port", port);
		map.put("appConfigKey", appConfigKey);
		map.put("linkTo", sub);
		return map;
	}

	@RequestMapping(value = "/feign", method = { RequestMethod.GET })
	public Map<String, Map<String, String>> feign() {
		Map<String, String> body = new HashMap<String, String>();
		body.put("timestamp", System.currentTimeMillis() + "");
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		map.put("get", serviceLinkClient.getParam("P1"));
		map.put("post", serviceLinkClient.setParam("P2", "Valueeeeee", body));
		return map;
	}
}
