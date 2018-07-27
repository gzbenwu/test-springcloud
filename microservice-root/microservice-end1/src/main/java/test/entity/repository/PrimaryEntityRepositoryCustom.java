package test.entity.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import test.entity.PrimaryEntity;

@Component
public class PrimaryEntityRepositoryCustom {
	@Autowired
	private MongoTemplate mongoTemplate;

	public List<PrimaryEntity> findByAggregationForVersion(long num, Pageable page) {
		List<AggregationOperation> opts = new ArrayList<>();
		MatchOperation matchStage = Aggregation.match(new Criteria("version").is(num));
		opts.add(matchStage);
		opts.add(Aggregation.sort(page.getSort()));
		opts.add(Aggregation.limit(page.getPageSize()));
		opts.add(Aggregation.skip((long) page.getOffset()));

		AggregationResults<PrimaryEntity> list = mongoTemplate.aggregate(Aggregation.newAggregation(opts), "primary_entity", PrimaryEntity.class);
		return list.getMappedResults();
	}
}
