package test.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

import test.entity.autoincrement.AutoIncrementId;
import test.entity.validator.TestValidatorAnnotation;
import test.entity.validator.ValidtorGroup_NeedCheck;

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
	@TestValidatorAnnotation(groups = { Default.class, ValidtorGroup_NeedCheck.class })
	private String primaryData;

	@Field("tData")
	@NotNull(message = "timeData cannot be null")
	@Future
	private Date timeData;

	@Field
	@DBRef(lazy = true)
	private SubEntity subEntity;

	@Field
	private SubEntity subEntityBody;

	@Transient
	private List<SubEntity> subEntityList;

	@Field
	@TestValidatorAnnotation(message = "Custome Validate Msg!")
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

	@JsonFormat(pattern = "HH:mm:ss!yyyy-MM-dd")
	public Date getTimeData() {
		return timeData;
	}

	@JsonFormat(pattern = "HH:mm:ss@yyyy-MM-dd")
	public void setTimeData(Date timeData) {
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

	public List<SubEntity> getSubEntityList() {
		return subEntityList;
	}

	public void setSubEntityList(List<SubEntity> subEntityList) {
		this.subEntityList = subEntityList;
	}
}
