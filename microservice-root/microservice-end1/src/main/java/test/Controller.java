package test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import test.entity.PrimaryEntity;
import test.entity.SubEntity;
import test.entity.repository.PrimaryEntityRepository;
import test.entity.repository.SubEntityRepository;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

	@Autowired
	private PrimaryEntityRepository primaryEntityRepository;
	@Autowired
	private SubEntityRepository subEntityRepository;

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

	@RequestMapping(value = "/saveOne", method = { RequestMethod.POST })
	public String saveOne() {
		SubEntity se = new SubEntity();
		se.setStringData(UUID.randomUUID().toString());
		se.setTimeData(LocalDateTime.now());
		se = subEntityRepository.save(se);

		PrimaryEntity pe = new PrimaryEntity();
		pe.setPrimaryData(UUID.randomUUID().toString());
		pe.setSubEntity(se);
		primaryEntityRepository.save(pe);
		return "OK";
	}
}
