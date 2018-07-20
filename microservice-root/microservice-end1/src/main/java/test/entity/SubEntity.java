package test.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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
	@JsonFormat(pattern = "yyyy|MM|dd@HH!mm!ss")
	private LocalDateTime timeData;

	@Transient
	private LocalDateTime defaultFormat;

	@Transient
	private Date defaultDate;

	@Transient
	private Date inOutDifferent;

	@Transient
	@JsonFormat(pattern = "HH$mm$ss$yyyy$MM$dd")
	private Map<String, List<Map<String, Date>>> defaultList1;

	@Transient
	private List<Map<String, List<LocalDateTime>>> defaultList2;

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

	public LocalDateTime getDefaultFormat() {
		return defaultFormat;
	}

	public void setDefaultFormat(LocalDateTime defaultFormat) {
		this.defaultFormat = defaultFormat;
	}

	public Date getDefaultDate() {
		return defaultDate;
	}

	public void setDefaultDate(Date defaultDate) {
		this.defaultDate = defaultDate;
	}

	@JsonFormat(pattern = "yyyyMMdd HHmmss")
	public Date getInOutDifferent() {
		return inOutDifferent;
	}

	@JsonFormat(pattern = "HH:mm:ss yyyy-MM-dd")
	public void setInOutDifferent(Date inOutDifferent) {
		this.inOutDifferent = inOutDifferent;
	}

	public Map<String, List<Map<String, Date>>> getDefaultList1() {
		return defaultList1;
	}

	public void setDefaultList1(Map<String, List<Map<String, Date>>> defaultList1) {
		this.defaultList1 = defaultList1;
	}

	public List<Map<String, List<LocalDateTime>>> getDefaultList2() {
		return defaultList2;
	}

	@JsonFormat(pattern = "yyyy_MM_dd_HH_mm_ss")
	public void setDefaultList2(List<Map<String, List<LocalDateTime>>> defaultList2) {
		this.defaultList2 = defaultList2;
	}
}
