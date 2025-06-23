package co.com.bancolombia.driven.adapter.mongo.repository;

import co.com.bancolombia.driven.adapter.mongo.entity.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoFranchiseRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
}
