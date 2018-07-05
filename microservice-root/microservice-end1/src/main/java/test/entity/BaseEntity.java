package test.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.repository.NoRepositoryBean;

import com.fasterxml.jackson.annotation.JsonFormat;

@NoRepositoryBean
public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = 3715010457208936143L;

	@Version
	@Field
	private Long version;

	@CreatedDate
	@Field
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime createdDate;

	@CreatedBy
	@Field
	private String createdBy;

	@LastModifiedDate
	@Field
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalDateTime lastModifiedDate;

	@LastModifiedBy
	@Field
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
