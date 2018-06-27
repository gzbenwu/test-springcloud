package test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

	@RequestMapping(value = "/serverLink", method = { RequestMethod.GET })
	public Map<String, String> getServerLink() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("test", "test");
		return map;
	}
}
