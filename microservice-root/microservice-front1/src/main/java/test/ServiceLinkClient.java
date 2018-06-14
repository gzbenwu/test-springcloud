package test;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "microservice-middle1")
public interface ServiceLinkClient {

	@RequestMapping(value = "/microservice-middle1/serverLink", method = { RequestMethod.GET })
	Map<String, Object> getSubServerLink();
}
