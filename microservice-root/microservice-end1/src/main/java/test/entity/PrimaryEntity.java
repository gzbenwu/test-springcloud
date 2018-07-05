package test.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

import test.entity.autoincrement.AutoIncrementId;

@Document(collection = "primary_entity")
public class PrimaryEntity extends BaseEntity {
	private static final long serialVersionUID = 7374861822147685961L;

	@Id
	@Field
	@AutoIncrementId
	@NotNull(message = "id cannot be null")
	@NotBlank(message = "id cannot be empty")
	private String id;

	@Field
	@NotNull(message = "primaryData cannot be null")
	@NotBlank(message = "primaryData cannot be empty")
	private String primaryData;

	@Field("tData")
	@NotNull(message = "timeData cannot be null")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timeData;

	@Field
	@DBRef
	private SubEntity subEntity;

	@Field
	private SubEntity subEntityBody;

	@Field
	private String subEntityId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrimaryData() {
		return primaryData;
	}

	public void setPrimaryData(String primaryData) {
		this.primaryData = primaryData;
	}

	public LocalDateTime getTimeData() {
		return timeData;
	}

	public void setTimeData(LocalDateTime timeData) {
		this.timeData = timeData;
	}

	public SubEntity getSubEntity() {
		return subEntity;
	}

	public void setSubEntity(SubEntity subEntity) {
		this.subEntity = subEntity;
	}

	public String getSubEntityId() {
		return subEntityId;
	}

	public void setSubEntityId(String subEntityId) {
		this.subEntityId = subEntityId;
	}

	public SubEntity getSubEntityBody() {
		return subEntityBody;
	}

	public void setSubEntityBody(SubEntity subEntityBody) {
		this.subEntityBody = subEntityBody;
	}
}
