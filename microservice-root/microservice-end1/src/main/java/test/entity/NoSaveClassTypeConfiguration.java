package test.entity;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class NoSaveClassTypeConfiguration {
	@Autowired
	private MappingMongoConverter mappingMongoConverter;

	@PostConstruct
	private void init() {
		mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
	}
}