package test.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.repository.NoRepositoryBean;

import com.fasterxml.jackson.annotation.JsonFormat;

import test.entity.validator.ValidtorGroup_NoNeedCheck;

@NoRepositoryBean
public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = 3715010457208936143L;

	@Version
	@Field
	private Long version;

	@CreatedDate
	@Field
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createdDate;

	@CreatedBy
	@Field
	private String createdBy;

	@LastModifiedDate
	@Field
	@JsonFormat(pattern = "MM/dd/yyyy-HH,mm,ss")
	private LocalDateTime lastModifiedDate;

	@LastModifiedBy
	@Field
	private String lastModifiedBy;

	// ====================================================================================
	@Null
	@NotNull(groups = ValidtorGroup_NoNeedCheck.class)
	private Object onlyForListAllConditions;

	@Transient
	@NotEmpty
	@NotBlank
	@Pattern(regexp = "[a-z-A-Z]*")
	@Length(min = 1, max = 5)
	@SafeHtml
	@Email(groups = ValidtorGroup_NoNeedCheck.class)
	@URL(groups = ValidtorGroup_NoNeedCheck.class)
	private String onlyForListAllConditionsString = "abc";

	@AssertFalse
	@AssertTrue
	private Boolean onlyForListAllConditionsBoolean;

	@Max(value = 999)
	@Min(value = 111)
	@Range(min = 1, max = 5)
	private Integer onlyForListAllConditionsLong;

	@DecimalMax(value = "1.001", inclusive = true)
	@DecimalMin(value = "0.001", inclusive = false)
	private Double onlyForListAllConditionsFloat;

	@Future
	@Past
	private Date onlyForListAllConditionsDate;

	@Size(min = 1, max = 5)
	private Set<?> onlyForListAllConditionsCollection;

	// ====================================================================================
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
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
