package test.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
public class User implements Serializable {
	private static final long serialVersionUID = 3161820665388130683L;

	@Id
	@Field
	@NotNull(message = "id cannot be null")
	@NotBlank(message = "id cannot be empty")
	private String id;

	@Field("name")
	@NotNull(message = "name cannot be null")
	@NotBlank(message = "name cannot be empty")
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
