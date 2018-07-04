package test.entity.autoincrement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "sequence")
public class SequenceId {
	@Id
	private String id;

	@Field("seq_id")
	private Long seqId;

	@Field("coll_name")
	private String collName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public String getCollName() {
		return collName;
	}

	public void setCollName(String collName) {
		this.collName = collName;
	}
}
