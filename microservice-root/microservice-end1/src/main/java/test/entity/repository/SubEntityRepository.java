package test.entity.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import test.entity.SubEntity;

public interface SubEntityRepository extends MongoRepository<SubEntity, String> {

}
