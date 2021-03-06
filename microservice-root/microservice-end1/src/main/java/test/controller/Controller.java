package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import test.ConfigrationPropertySource;
import test.aop.TestService;
import test.bean.BigBean;
import test.entity.PrimaryEntity;
import test.entity.SubEntity;
import test.entity.repository.PrimaryEntityRepository;
import test.entity.repository.PrimaryEntityRepositoryCustom;
import test.entity.repository.SubEntityRepository;
import test.entity.validator.ValidtorGroup_NeedCheck;
import test.entity.validator.ValidtorGroup_NoNeedCheck;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

@RestController
public class Controller {

	@Value("${server.port}")
	private String port;

	@Value("${custom.serverid}")
	private String test;

	@Value("${git.props}")
	private String gitProps;

	@Autowired
	private ConfigrationPropertySource configrationPropertySource;
	@Autowired
	private Validator validator;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private PrimaryEntityRepository primaryEntityRepository;
	@Autowired
	private SubEntityRepository subEntityRepository;
	@Autowired
	private PrimaryEntityRepositoryCustom primaryEntityRepositoryImpl;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private TestService testService;

	@RequestMapping(value = "/serverLink", method = { RequestMethod.GET })
	public String getServerLink(HttpServletRequest req) throws JsonProcessingException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("port", port);
		map.put("appConfigKey", configrationPropertySource.getAppConfigKey());
		map.put("test", test);
		map.put("localAppProp", configrationPropertySource.getApplication());
		map.put("gitProps", gitProps);
		Map<String, Object> headers = new HashMap<String, Object>();
		Enumeration<String> hns = req.getHeaderNames();
		while (hns.hasMoreElements()) {
			String header = hns.nextElement();
			headers.put(header, req.getHeader(header));
		}
		map.put("RequestHeaders_inend", headers);
		return objectMapper.writeValueAsString(map);
	}

	@RequestMapping(value = "/saveOne", method = { RequestMethod.POST })
	public String saveOne() {
		SubEntity se = new SubEntity();
		se.setStringData(UUID.randomUUID().toString());
		se.setTimeData(LocalDateTime.now());
		se = subEntityRepository.save(se);

		PrimaryEntity pe = new PrimaryEntity();
		pe.setPrimaryData(UUID.randomUUID().toString());
		pe.setTimeData(new Date());
		pe.setSubEntity(se);
		pe.setSubEntityId(se.getId());
		pe.setSubEntityBody(se);
		primaryEntityRepository.save(pe);
		return "saveOne";
	}

	@RequestMapping(value = "/updatePrimary", method = { RequestMethod.POST })
	public String saveOne(@Validated({ ValidtorGroup_NoNeedCheck.class }) @RequestBody PrimaryEntity entity) {
		StringBuilder sb = new StringBuilder();
		Set<ConstraintViolation<PrimaryEntity>> result = validator.validate(entity, new Class[] { ValidtorGroup_NeedCheck.class });
		for (ConstraintViolation<PrimaryEntity> cv : result) {
			sb.append(cv.getMessageTemplate()).append(", ");
		}

		PrimaryEntity pe = primaryEntityRepository.findOne(entity.getId());
		pe.setPrimaryData(entity.getPrimaryData());
		pe.setTimeData(entity.getTimeData());
		primaryEntityRepository.save(pe);
		return sb.toString().isEmpty() ? "updatePrimary" : sb.toString();
	}

	@RequestMapping(value = "/getPrimary/{id}", method = { RequestMethod.GET })
	public PrimaryEntity getOne(@PathVariable("id") String id) throws Exception {
		PrimaryEntity pe = primaryEntityRepository.findOne(id);
		// Lazy模式下，当调用该对象的时候才会再次到MongoDB中获取对象数据。如果在这里把该代理对象去掉，就可以不再关联了。
		pe.setSubEntity(null);
		StringBuilder sb = testService.doSomething("What?", 100);
		System.out.println("Controller..........." + sb.toString());
		return pe;
	}

	@RequestMapping(value = "/getPrimaryList1/{pageSize}/{version}/{subEntityId}", method = { RequestMethod.GET })
	public List<PrimaryEntity> getList1(@PathVariable("pageSize") int pageSize, @PathVariable("version") Long version, @PathVariable("subEntityId") String subEntityId) throws Exception {
		Pageable page = new PageRequest(0, pageSize, new Sort(new Order(Sort.Direction.DESC, "createdDate"), new Order(Sort.Direction.ASC, "primaryData")));
		List<PrimaryEntity> pes = primaryEntityRepository.findByCustom(version, subEntityId, page);
		pes.stream().forEach(pe -> pe.setSubEntity(null));
		return pes;
	}

	@RequestMapping(value = "/getPrimaryList2", method = { RequestMethod.POST })
	public List<PrimaryEntity> getList2(@RequestBody Map<String, Object> body) throws Exception {
		Pageable page = new PageRequest(0, new Integer((String) body.get("pageSize")).intValue(), new Sort(new Order(Sort.Direction.DESC, "createdDate"), new Order(Sort.Direction.ASC, "primaryData")));
		@SuppressWarnings("unchecked")
		List<Integer> vers = (List<Integer>) body.get("versions");
		List<Long> list = vers.stream().map(ver -> ver.longValue()).collect(Collectors.toList());
		List<PrimaryEntity> pes = primaryEntityRepository.findByVersionIn(list, page);
		pes.stream().forEach(pe -> pe.setSubEntity(null));
		return pes;
	}

	@RequestMapping(value = "/getPrimaryList3", method = { RequestMethod.GET })
	public List<PrimaryEntity> getList3(@RequestParam long version, @RequestParam int start, @RequestParam int size) throws Exception {
		Pageable page = new PageRequest(start, size, new Sort(new Order(Sort.Direction.DESC, "createdDate"), new Order(Sort.Direction.ASC, "primaryData")));
		List<PrimaryEntity> pes = primaryEntityRepositoryImpl.findByAggregationForVersion(version, page);
		pes.stream().forEach(pe -> pe.setSubEntity(null));
		return pes;
	}

	@RequestMapping(value = "/updateSub", method = { RequestMethod.POST })
	public String saveOne(@Valid @RequestBody SubEntity entity, BindingResult validResult) {
		String msg = RestExceptionHandler.handleArgumentNotValid(validResult);
		if (msg != null) {
			return "[Build By Controller] " + msg;
		}

		SubEntity pe = subEntityRepository.findOne(entity.getId());
		pe.setStringData(entity.getStringData());
		pe.setTimeData(entity.getTimeData());
		subEntityRepository.save(pe);
		return "updateSub";
	}

	@RequestMapping(value = "/transformOne", method = { RequestMethod.POST })
	public String transformOne(@RequestBody String entity) {
		return "{\r\n" + "	\"map\": {\r\n" + "		\"aaKey\": {\r\n" + "			\"id\": \"aaaaaa\",\r\n" + "			\"primaryData\": \"---+++----\",\r\n" + "			\"timeData\": \"12:12:12@2018-12-12\",\r\n" + "			\"subEntityId\": \"1234567890\",\r\n" + "			\"subEntity\": {\r\n" + "				\"timeData\": \"2012|12|12@12!12!12\",\r\n" + "				\"defaultFormat\": \"2013-11-13 13:13:13\",\r\n" + "				\"defaultDate\": \"2014-11-14 14:14:14\",\r\n" + "				\"inOutDifferent\": \"15:15:15 2015-11-15\",\r\n" + "				\"createdDate\": \"2016-11-16##16*16*16\",\r\n" + "				\"lastModifiedDate\": \"11/17/2017-17,17,17\",\r\n" + "				\"defaultList1\": {\r\n" + "					\"y\": [\r\n" + "						{\r\n" + "							\"y1\": \"18$18$18$2118$11$18\",\r\n" + "							\"y2\": \"18$18$18$2118$11$18\"\r\n" + "						},\r\n" + "						{\r\n" + "							\"y3\": \"18$18$18$2118$11$18\",\r\n" + "							\"y4\": \"18$18$18$2118$11$18\"\r\n" + "						}\r\n" + "					],\r\n" + "					\"z\": [\r\n" + "						{\r\n" + "							\"z1\": \"18$18$18$2118$11$18\",\r\n" + "							\"z2\": \"18$18$18$2118$11$18\"\r\n" + "						},\r\n" + "						{\r\n" + "							\"z3\": \"18$18$18$2118$11$18\",\r\n" + "							\"z4\": \"18$18$18$2118$11$18\"\r\n" + "						}\r\n" + "					]\r\n" + "				},\r\n" + "				\"defaultList2\": [\r\n" + "					{\r\n" + "						\"a\": [\r\n" + "							\"2319_11_19_19_19_19\",\r\n" + "							\"2319_11_19_19_19_19\"\r\n" + "						],\r\n" + "						\"b\": [\r\n" + "							\"2319_11_19_19_19_19\",\r\n" + "							\"2319_11_19_19_19_19\"\r\n" + "						]\r\n" + "					},\r\n" + "					{\r\n" + "						\"c\": [\r\n" + "							\"2319_11_19_19_19_19\",\r\n" + "							\"2319_11_19_19_19_19\"\r\n" + "						],\r\n" + "						\"d\": [\r\n" + "							\"2319_11_19_19_19_19\",\r\n" + "							\"2319_11_19_19_19_19\"\r\n" + "						]\r\n" + "					}\r\n" + "				]\r\n" + "			},\r\n" + "			\"subEntityList\": [\r\n" + "				{\r\n" + "					\"timeData\": \"2012|12|12@12!12!12\",\r\n" + "					\"defaultFormat\": \"2013-11-13 13:13:13\",\r\n" + "					\"defaultDate\": \"2014-11-14 14:14:14\",\r\n" + "					\"inOutDifferent\": \"15:15:15 2015-11-15\",\r\n" + "					\"createdDate\": \"2016-11-16##16*16*16\",\r\n" + "					\"lastModifiedDate\": \"11/17/2017-17,17,17\",\r\n" + "					\"defaultList1\": {\r\n" + "						\"y\": [\r\n" + "							{\r\n" + "								\"y1\": \"18$18$18$2118$11$18\",\r\n" + "								\"y2\": \"18$18$18$2118$11$18\"\r\n" + "							},\r\n" + "							{\r\n" + "								\"y3\": \"18$18$18$2118$11$18\",\r\n" + "								\"y4\": \"18$18$18$2118$11$18\"\r\n" + "							}\r\n" + "						],\r\n" + "						\"z\": [\r\n" + "							{\r\n" + "								\"z1\": \"18$18$18$2118$11$18\",\r\n" + "								\"z2\": \"18$18$18$2118$11$18\"\r\n" + "							},\r\n" + "							{\r\n" + "								\"z3\": \"18$18$18$2118$11$18\",\r\n" + "								\"z4\": \"18$18$18$2118$11$18\"\r\n" + "							}\r\n" + "						]\r\n" + "					},\r\n" + "					\"defaultList2\": [\r\n" + "						{\r\n" + "							\"a\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							],\r\n" + "							\"b\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							]\r\n" + "						},\r\n" + "						{\r\n" + "							\"c\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							],\r\n" + "							\"d\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							]\r\n" + "						}\r\n" + "					]\r\n" + "				},\r\n" + "				{\r\n" + "					\"timeData\": \"2013|12|12@12!12!12\",\r\n" + "					\"defaultFormat\": \"2013-11-13 13:13:13\",\r\n" + "					\"defaultDate\": \"2014-11-14 14:14:14\",\r\n" + "					\"inOutDifferent\": \"15:15:15 2015-11-15\",\r\n" + "					\"createdDate\": \"2016-11-16##16*16*16\",\r\n" + "					\"lastModifiedDate\": \"11/17/2017-17,17,17\",\r\n" + "					\"defaultList1\": {\r\n" + "						\"y\": [\r\n" + "							{\r\n" + "								\"y1\": \"18$18$18$2118$11$18\",\r\n" + "								\"y2\": \"18$18$18$2118$11$18\"\r\n" + "							},\r\n" + "							{\r\n" + "								\"y3\": \"18$18$18$2118$11$18\",\r\n" + "								\"y4\": \"18$18$18$2118$11$18\"\r\n" + "							}\r\n" + "						],\r\n" + "						\"z\": [\r\n" + "							{\r\n" + "								\"z1\": \"18$18$18$2118$11$18\",\r\n" + "								\"z2\": \"18$18$18$2118$11$18\"\r\n" + "							},\r\n" + "							{\r\n" + "								\"z3\": \"18$18$18$2118$11$18\",\r\n" + "								\"z4\": \"18$18$18$2118$11$18\"\r\n" + "							}\r\n" + "						]\r\n" + "					},\r\n" + "					\"defaultList2\": [\r\n" + "						{\r\n" + "							\"a\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							],\r\n" + "							\"b\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							]\r\n" + "						},\r\n" + "						{\r\n" + "							\"c\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							],\r\n" + "							\"d\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							]\r\n" + "						}\r\n" + "					]\r\n" + "				}\r\n" + "			]\r\n" + "		},\r\n" + "		\"bbKey\": {\r\n" + "			\"id\": \"bbbbbbbbbbb\",\r\n" + "			\"primaryData\": \"---+++----\",\r\n" + "			\"timeData\": \"12:12:12@2018-12-12\",\r\n" + "			\"subEntityId\": \"1234567890\",\r\n" + "			\"subEntity\": {\r\n" + "				\"timeData\": \"2012|12|12@12!12!12\",\r\n" + "				\"defaultFormat\": \"2013-11-13 13:13:13\",\r\n" + "				\"defaultDate\": \"2014-11-14 14:14:14\",\r\n" + "				\"inOutDifferent\": \"15:15:15 2015-11-15\",\r\n" + "				\"createdDate\": \"2016-11-16##16*16*16\",\r\n" + "				\"lastModifiedDate\": \"11/17/2017-17,17,17\",\r\n" + "				\"defaultList1\": {\r\n" + "					\"y\": [\r\n" + "						{\r\n" + "							\"y1\": \"18$18$18$2118$11$18\",\r\n" + "							\"y2\": \"18$18$18$2118$11$18\"\r\n" + "						},\r\n" + "						{\r\n" + "							\"y3\": \"18$18$18$2118$11$18\",\r\n" + "							\"y4\": \"18$18$18$2118$11$18\"\r\n" + "						}\r\n" + "					],\r\n" + "					\"z\": [\r\n" + "						{\r\n" + "							\"z1\": \"18$18$18$2118$11$18\",\r\n" + "							\"z2\": \"18$18$18$2118$11$18\"\r\n" + "						},\r\n" + "						{\r\n" + "							\"z3\": \"18$18$18$2118$11$18\",\r\n" + "							\"z4\": \"18$18$18$2118$11$18\"\r\n" + "						}\r\n" + "					]\r\n" + "				},\r\n" + "				\"defaultList2\": [\r\n" + "					{\r\n" + "						\"a\": [\r\n" + "							\"2319_11_19_19_19_19\",\r\n" + "							\"2319_11_19_19_19_19\"\r\n" + "						],\r\n" + "						\"b\": [\r\n" + "							\"2319_11_19_19_19_19\",\r\n" + "							\"2319_11_19_19_19_19\"\r\n"
				+ "						]\r\n" + "					},\r\n" + "					{\r\n" + "						\"c\": [\r\n" + "							\"2319_11_19_19_19_19\",\r\n" + "							\"2319_11_19_19_19_19\"\r\n" + "						],\r\n" + "						\"d\": [\r\n" + "							\"2319_11_19_19_19_19\",\r\n" + "							\"2319_11_19_19_19_19\"\r\n" + "						]\r\n" + "					}\r\n" + "				]\r\n" + "			},\r\n" + "			\"subEntityList\": [\r\n" + "				{\r\n" + "					\"timeData\": \"2012|12|12@12!12!12\",\r\n" + "					\"defaultFormat\": \"2013-11-13 13:13:13\",\r\n" + "					\"defaultDate\": \"2014-11-14 14:14:14\",\r\n" + "					\"inOutDifferent\": \"15:15:15 2015-11-15\",\r\n" + "					\"createdDate\": \"2016-11-16##16*16*16\",\r\n" + "					\"lastModifiedDate\": \"11/17/2017-17,17,17\",\r\n" + "					\"defaultList1\": {\r\n" + "						\"y\": [\r\n" + "							{\r\n" + "								\"y1\": \"18$18$18$2118$11$18\",\r\n" + "								\"y2\": \"18$18$18$2118$11$18\"\r\n" + "							},\r\n" + "							{\r\n" + "								\"y3\": \"18$18$18$2118$11$18\",\r\n" + "								\"y4\": \"18$18$18$2118$11$18\"\r\n" + "							}\r\n" + "						],\r\n" + "						\"z\": [\r\n" + "							{\r\n" + "								\"z1\": \"18$18$18$2118$11$18\",\r\n" + "								\"z2\": \"18$18$18$2118$11$18\"\r\n" + "							},\r\n" + "							{\r\n" + "								\"z3\": \"18$18$18$2118$11$18\",\r\n" + "								\"z4\": \"18$18$18$2118$11$18\"\r\n" + "							}\r\n" + "						]\r\n" + "					},\r\n" + "					\"defaultList2\": [\r\n" + "						{\r\n" + "							\"a\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							],\r\n" + "							\"b\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							]\r\n" + "						},\r\n" + "						{\r\n" + "							\"c\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							],\r\n" + "							\"d\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							]\r\n" + "						}\r\n" + "					]\r\n" + "				},\r\n" + "				{\r\n" + "					\"timeData\": \"2013|12|12@12!12!12\",\r\n" + "					\"defaultFormat\": \"2013-11-13 13:13:13\",\r\n" + "					\"defaultDate\": \"2014-11-14 14:14:14\",\r\n" + "					\"inOutDifferent\": \"15:15:15 2015-11-15\",\r\n" + "					\"createdDate\": \"2016-11-16##16*16*16\",\r\n" + "					\"lastModifiedDate\": \"11/17/2017-17,17,17\",\r\n" + "					\"defaultList1\": {\r\n" + "						\"y\": [\r\n" + "							{\r\n" + "								\"y1\": \"18$18$18$2118$11$18\",\r\n" + "								\"y2\": \"18$18$18$2118$11$18\"\r\n" + "							},\r\n" + "							{\r\n" + "								\"y3\": \"18$18$18$2118$11$18\",\r\n" + "								\"y4\": \"18$18$18$2118$11$18\"\r\n" + "							}\r\n" + "						],\r\n" + "						\"z\": [\r\n" + "							{\r\n" + "								\"z1\": \"18$18$18$2118$11$18\",\r\n" + "								\"z2\": \"18$18$18$2118$11$18\"\r\n" + "							},\r\n" + "							{\r\n" + "								\"z3\": \"18$18$18$2118$11$18\",\r\n" + "								\"z4\": \"18$18$18$2118$11$18\"\r\n" + "							}\r\n" + "						]\r\n" + "					},\r\n" + "					\"defaultList2\": [\r\n" + "						{\r\n" + "							\"a\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							],\r\n" + "							\"b\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							]\r\n" + "						},\r\n" + "						{\r\n" + "							\"c\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							],\r\n" + "							\"d\": [\r\n" + "								\"2319_11_19_19_19_19\",\r\n" + "								\"2319_11_19_19_19_19\"\r\n" + "							]\r\n" + "						}\r\n" + "					]\r\n" + "				}\r\n" + "			]\r\n" + "		}\r\n" + "	}\r\n" + "}";
	}

	@RequestMapping(value = "/transformRestTemplate/{date1}", method = { RequestMethod.POST })
	public BigBean transformRestTemplate(@PathVariable("date1") LocalDateTime date1, @RequestParam("date2") Date date2, @Valid @RequestBody BigBean json) throws URISyntaxException {
		HttpHeaders hh = new HttpHeaders();
		hh.setContentType(MediaType.APPLICATION_JSON_UTF8);
		RequestEntity<Object> re = new RequestEntity<Object>(json, hh, HttpMethod.POST, new URI("http://microservice-end1/transformOne"));
		ResponseEntity<BigBean> res = restTemplate.exchange(re, BigBean.class);
		return res.getBody();
	}
}
