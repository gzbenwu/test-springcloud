package test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

	@Value("${custom.test.appConfigKey}")
	private String appConfigKey;

	@Value("${server.port}")
	private String port;

	@Value("${custom.serverid}")
	private String test;

	@Value("${git.props}")
	private String gitProps;

	@Value("${local.application}")
	private String localAppProps;

	@RequestMapping(value = "/serverLink", method = { RequestMethod.GET })
	public Map<String, String> getServerLink() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("port", port);
		map.put("appConfigKey", appConfigKey);
		map.put("test", test);
		map.put("localAppProp", localAppProps);
		map.put("gitProps", gitProps);
		return map;
	}
}
