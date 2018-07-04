package test.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import test.entity.autoincrement.AutoIncrementId;

@Document(collection = "primary_entity")
public class PrimaryEntity extends BaseEntity {
	private static final long serialVersionUID = 7374861822147685961L;

	@Id
	@Field
	@AutoIncrementId
	@NotNull(message = "id cannot be null")
	private String id;

	@Field
	@NotNull(message = "primaryData cannot be null")
	private String primaryData;

	@Field("tData")
	@NotNull(message = "timeData cannot be null")
	private LocalDateTime timeData;

	@Field
	@DBRef
	private SubEntity subEntity;

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
		if (subEntity == null) {
			this.subEntityId = null;
		} else {
			this.subEntityId = subEntity.getId();
		}
	}

	public String getSubEntityId() {
		return subEntityId;
	}
}
