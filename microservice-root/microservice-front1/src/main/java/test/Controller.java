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
    private ServiceLinkClient serviceLinkClient;


    @RequestMapping(value = "/serverLink", method = {RequestMethod.GET})
    public Map<String, Object> getServerLink() {
        Map<String, Object> sub = serviceLinkClient.getSubServerLink();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("port", port);
        map.put("appConfigKey", appConfigKey);
        map.put("linkTo", sub);
        return map;
    }
}
