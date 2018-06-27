package test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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

	@Value("${custom.testpw}")
	private String testpw;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private LoadBalancerClient loadBalancerClient;

	@RequestMapping(value = "/serverLink", method = { RequestMethod.GET })
	public Map<String, Object> getServerLink() {
		Map<String, String> end1 = restTemplate.getForObject("http://microservice-end1/microservice-end1/serverLink", Map.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("port", port);
		map.put("appConfigKey", appConfigKey);
		map.put("linkTo", end1);
		map.put("testpw", testpw);
		ServiceInstance serviceInstance = loadBalancerClient.choose("microservice-end1");
		map.put("chooseServiceMeta", serviceInstance.getMetadata());
		map.put("chooseServicePort", serviceInstance.getPort());
		return map;
	}
}
