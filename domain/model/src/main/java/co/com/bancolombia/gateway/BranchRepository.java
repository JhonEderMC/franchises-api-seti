package co.com.bancolombia.gateway;

import co.com.bancolombia.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Mono<Branch> save(Branch branch);
    Mono<Branch> findById(String id);
    Flux<Branch> findByFranchiseId(String franchiseId);
    Mono<Branch> update(Branch branch);
    Mono<Void> deleteById(String id);
}