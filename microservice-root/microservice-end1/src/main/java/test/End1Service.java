package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringCloudApplication
@EnableMongoAuditing
@ComponentScan(basePackages = { "test" }, excludeFilters = { @Filter(type = FilterType.ASSIGNABLE_TYPE, value = { ExcludedConfig.class }) })
public class End1Service {
	public static void main(String[] args) {
		SpringApplication.run(End1Service.class, args);
	}

	@Bean(initMethod = "hashCode")
	@LoadBalanced
	@Autowired
	public RestTemplate restTemplate(ObjectMapper objectMapper) {
		RestTemplate restTemplate = new RestTemplate();

		// remove the default MappingJackson2HttpMessageConverter
		List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
		if (list == null) {
			list = new ArrayList<HttpMessageConverter<?>>();
		}
		Iterator<HttpMessageConverter<?>> i = list.iterator();
		while (i.hasNext()) {
			HttpMessageConverter<?> hmc = i.next();
			if (hmc instanceof MappingJackson2HttpMessageConverter) {
				i.remove();
			}
		}

		// add the custom Date format converter
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		jsonConverter.setObjectMapper(objectMapper);
		list.add(jsonConverter);

		return restTemplate;
	}
}
