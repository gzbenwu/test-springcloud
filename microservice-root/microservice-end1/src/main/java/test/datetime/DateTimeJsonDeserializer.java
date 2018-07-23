package test.datetime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateTimeJsonDeserializer<T> extends JsonDeserializer<T> {
	private Class<T> clazz;

	public DateTimeJsonDeserializer(Class<T> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		final DateTimeFormatterHolder holder = new DateTimeFormatterHolder();
		holder.jsc = p.getParsingContext();
		prepareDateTimeFormatterHolder(holder, "set");

		if (holder.format == null) {
			holder.format = SystemDefaultDateTimeFormater.format;
		}

		T dateTime = null;
		String source = p.getValueAsString();
		if (LocalDateTime.class.equals(clazz)) {
			dateTime = (T) LocalDateTime.parse(source, DateTimeFormatter.ofPattern(holder.format));
		} else if (Date.class.equals(clazz)) {
			try {
				dateTime = (T) new SimpleDateFormat(holder.format).parse(source);
			} catch (ParseException e) {
				throw new IllegalArgumentException(e.getMessage(), e);
			}
		}
		return dateTime;
	}

	public static void prepareDateTimeFormatterHolder(DateTimeFormatterHolder holder, String methodType) {
		if (holder.jsc.getCurrentName() == null || holder.jsc.getCurrentValue() == null || holder.jsc.getCurrentValue() instanceof Map || holder.jsc.getCurrentValue() instanceof Collection) {
			for (int i = 0; i < 10; i++) {
				holder.jsc = holder.jsc.getParent();
				if (holder.jsc == null || (holder.jsc.getCurrentName() != null && holder.jsc.getCurrentValue() != null && !(holder.jsc.getCurrentValue() instanceof Map) && !(holder.jsc.getCurrentValue() instanceof Collection))) {
					break;
				}
			}
		}
		if (holder.jsc != null && holder.jsc.getCurrentName() != null && holder.jsc.getCurrentValue() != null && !(holder.jsc.getCurrentValue() instanceof Map) && !(holder.jsc.getCurrentValue() instanceof Collection)) {
			ReflectionUtils.doWithMethods(holder.jsc.getCurrentValue().getClass(), (method) -> {
				JsonFormat jf = method.getAnnotation(JsonFormat.class);
				if (jf != null && jf.pattern() != null) {
					holder.format = jf.pattern();
				}
			}, (method) -> {
				return method.getName().equalsIgnoreCase(methodType.concat(holder.jsc.getCurrentName()));
			});

			if (holder.format == null) {
				ReflectionUtils.doWithFields(holder.jsc.getCurrentValue().getClass(), (field) -> {
					JsonFormat jf = field.getAnnotation(JsonFormat.class);
					if (jf != null && jf.pattern() != null) {
						holder.format = jf.pattern();
					}
				}, (field) -> {
					return field.getName().equals(holder.jsc.getCurrentName());
				});
			}
		}
	}
}
