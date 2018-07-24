package test.entity.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import test.entity.PrimaryEntity;

public interface PrimaryEntityRepository extends MongoRepository<PrimaryEntity, String> {
	List<PrimaryEntity> findByVersionAndPrimaryData(Long version, String primaryData);

	List<PrimaryEntity> findByVersionAndCreatedDateBetween(Long version, Date start, Date end);

	List<PrimaryEntity> findByVersionIn(List<Long> version, Pageable page);

	@Query(value = "{'version' : ?0, 'subEntityId' : ?1}")
	List<PrimaryEntity> findByCustom(Long version, String subEntityId, Pageable page);
}
