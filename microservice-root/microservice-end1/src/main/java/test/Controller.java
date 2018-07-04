package test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

	@Value("${custom.serverid}")
	private String test;

	@Value("${git.props}")
	private String gitProps;

	@Value("${local.application}")
	private String localAppProps;

	@RequestMapping(value = "/serverLink", method = { RequestMethod.GET })
	public Map<String, Object> getServerLink(HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("port", port);
		map.put("appConfigKey", appConfigKey);
		map.put("test", test);
		map.put("localAppProp", localAppProps);
		map.put("gitProps", gitProps);
		Map<String, Object> headers = new HashMap<String, Object>();
		Enumeration<String> hns = req.getHeaderNames();
		while (hns.hasMoreElements()) {
			String header = hns.nextElement();
			headers.put(header, req.getHeader(header));
		}
		map.put("RequestHeaders_inend", headers);
		return map;
	}
}
