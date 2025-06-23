package co.com.bancolombia.driven.adapter.mongo.repository;

import co.com.bancolombia.driven.adapter.mongo.entity.BranchDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MongoBranchRepository extends ReactiveMongoRepository<BranchDocument, String> {
    Flux<BranchDocument> findByFranchiseId(String franchiseId);
}