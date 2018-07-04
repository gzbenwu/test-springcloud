package test.entity.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import test.entity.PrimaryEntity;

public interface PrimaryEntityRepository extends MongoRepository<PrimaryEntity, String> {

}
