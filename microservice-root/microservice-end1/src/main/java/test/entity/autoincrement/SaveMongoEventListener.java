package test.entity.autoincrement;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import test.entity.BaseEntity;

@Component
public class SaveMongoEventListener extends AbstractMongoEventListener<BaseEntity> {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void onBeforeConvert(BeforeConvertEvent<BaseEntity> event) {
		BaseEntity entity = event.getSource();
		if (entity != null) {
			ReflectionUtils.doWithFields(entity.getClass(), new ReflectionUtils.FieldCallback() {
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					ReflectionUtils.makeAccessible(field);
					if (field.isAnnotationPresent(AutoIncrementId.class) && field.get(entity) == null) {
						field.set(entity, getNextId(event.getCollectionName()));
					}
				}
			});
		}
	}

	private String getNextId(String collName) {
		Query query = new Query(Criteria.where("collName").is(collName));
		Update update = new Update();
		update.inc("seqId", 1);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.upsert(true);
		options.returnNew(true);
		SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
		return "" + seqId.getSeqId();
	}
}
