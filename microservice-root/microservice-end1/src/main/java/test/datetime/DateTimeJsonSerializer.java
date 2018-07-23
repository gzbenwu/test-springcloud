package test.datetime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeJsonSerializer<T> extends JsonSerializer<T> {
	@Override
	public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		final DateTimeFormatterHolder holder = new DateTimeFormatterHolder();
		holder.jsc = gen.getOutputContext();
		DateTimeJsonDeserializer.prepareDateTimeFormatterHolder(holder, "get");

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
