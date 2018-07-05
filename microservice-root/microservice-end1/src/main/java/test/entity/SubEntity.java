package test.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "sub_entity")
public class SubEntity extends BaseEntity {
	private static final long serialVersionUID = 7374861822147685961L;

	@Id
	@Field
	@NotNull(message = "id cannot be null")
	@NotBlank(message = "id cannot be empty")
	private String id;

	@Field("sData")
	@NotNull(message = "stringData cannot be null")
	@NotBlank(message = "stringData cannot be empty")
	private String stringData;

	@Field
	@NotNull(message = "timeData cannot be null")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timeData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStringData() {
		return stringData;
	}

	public void setStringData(String stringData) {
		this.stringData = stringData;
	}

	public LocalDateTime getTimeData() {
		return timeData;
	}

	public void setTimeData(LocalDateTime timeData) {
		this.timeData = timeData;
	}
}
