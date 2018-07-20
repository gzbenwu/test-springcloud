package test.datetime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeJsonSerializer<T> extends JsonSerializer<T> {
	@Override
	public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		final DateTimeFormatterHolder holder = new DateTimeFormatterHolder();
		holder.jsc = gen.getOutputContext();
		if (holder.jsc.getCurrentName() == null || holder.jsc.getCurrentValue() == null) {
			for (int i = 0; i < 10; i++) {
				holder.jsc = holder.jsc.getParent();
				if (holder.jsc == null || (holder.jsc.getCurrentName() != null && holder.jsc.getCurrentValue() != null)) {
					break;
				}
			}
		}
		if (holder.jsc != null && holder.jsc.getCurrentName() != null && holder.jsc.getCurrentValue() != null) {
			ReflectionUtils.doWithMethods(holder.jsc.getCurrentValue().getClass(), (method) -> {
				JsonFormat jf = method.getAnnotation(JsonFormat.class);
				if (jf != null && jf.pattern() != null) {
					holder.format = jf.pattern();
				}
			}, (method) -> {
				return method.getName().equalsIgnoreCase("get".concat(holder.jsc.getCurrentName()));
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

		if (holder.format == null) {
			holder.format = SystemDefaultDateTimeFormater.format;
		}

		String text;
		if (LocalDateTime.class.equals(value.getClass())) {
			LocalDateTime ldt = (LocalDateTime) value;
			text = ldt.format(DateTimeFormatter.ofPattern(holder.format));
		} else {
			text = new SimpleDateFormat(holder.format).format(value);
		}

		gen.writeString(text);
	}
}
