package test;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "microservice-middle1")
public interface ServiceLinkClient {

	@RequestMapping(value = "/serverLink", method = { RequestMethod.GET })
	Map<String, Object> getSubServerLink();

	@RequestMapping(value = "/param/{paramName}/get", method = { RequestMethod.GET })
	Map<String, Object> getParam(@PathVariable("paramName") String paramName, @RequestHeader("H1") String h1);

	@RequestMapping(value = "/param/{paramName}/post/{paramValue}", method = { RequestMethod.POST })
	Map<String, Object> setParam(@PathVariable("paramName") String paramName, @PathVariable("paramValue") String paramValue, @RequestBody Map<String, Object> body);
}
