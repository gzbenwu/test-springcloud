package test.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class SystemDefaultDateTimeFormater {
	public static final String format = "yyyy-MM-dd HH:mm:ss";

	// Spring默认使用ObjectMapper来转Json,所以这里的默认配置只针对@RequestBody。对@PathVariable,@RequestParam不起作用
	@Bean
	public ObjectMapper serializingObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		// pom.xml中引入了jackson-datatype-jsr310等的类型转换器，这里可以自动扫描加载这些默认转换器。
		objectMapper.findAndRegisterModules();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		// 这里只对Date类型转换有效，如果Bean中定义为LocalDateTime,ZonedDateTime等类型都无效
		// SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// objectMapper.setDateFormat(smt);

		// jackson-datatype-jsr310的补充和复盖，修改默认的日期格式
		SimpleModule defaultDateTimeModule = new SimpleModule();
		defaultDateTimeModule.addDeserializer(LocalDateTime.class, new DateTimeJsonDeserializer<>(LocalDateTime.class));
		defaultDateTimeModule.addSerializer(LocalDateTime.class, new DateTimeJsonSerializer<>());
		defaultDateTimeModule.addDeserializer(Date.class, new DateTimeJsonDeserializer<>(Date.class));
		defaultDateTimeModule.addSerializer(Date.class, new DateTimeJsonSerializer<>());

		objectMapper.registerModule(defaultDateTimeModule);
		return objectMapper;
	}

	// 默认配置，只对@PathVariable,@RequestParam起作用，不会作用于@RequestBody
	@Autowired
	public void localDateTimeConvert(FormatterRegistry registry) {
		Converter<String, LocalDateTime> toLocalDateTime = new Converter<String, LocalDateTime>() {
			@Override
			public LocalDateTime convert(String source) {
				LocalDateTime localDateTime = LocalDateTime.parse(source, DateTimeFormatter.ofPattern(format));
				return localDateTime;
			}
		};
		Converter<String, Date> toDate = new Converter<String, Date>() {
			@Override
			public Date convert(String source) {
				Date date;
				try {
					date = new SimpleDateFormat(format).parse(source);
				} catch (ParseException e) {
					throw new IllegalArgumentException(e.getMessage(), e);
				}
				return date;
			}
		};
		registry.addConverter(String.class, LocalDateTime.class, toLocalDateTime);
		registry.addConverter(String.class, Date.class, toDate);
	}
}
