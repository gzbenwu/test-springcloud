package test.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;

public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = 3715010457208936143L;

	@Version
	@Field
	@NotNull(message = "version cannot be null")
	private Long version;

	@CreatedDate
	@Field
	@NotNull(message = "createdDate cannot be null")
	private LocalDateTime createdDate;

	@CreatedBy
	@Field
	@NotNull(message = "createdBy cannot be null")
	private String createdBy;

	@LastModifiedDate
	@Field
	@NotNull(message = "lastModifiedDate cannot be null")
	private LocalDateTime lastModifiedDate;

	@LastModifiedBy
	@Field
	@NotNull(message = "lastModifiedBy cannot be null")
	private String lastModifiedBy;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
