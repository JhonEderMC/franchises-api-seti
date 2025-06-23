package co.com.bancolombia.driven.adapter.mongo;

import co.com.bancolombia.driven.adapter.mongo.mapper.BranchMapper;
import co.com.bancolombia.driven.adapter.mongo.repository.MongoBranchRepository;
import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.model.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchMongoAdapter implements BranchRepository {

    private final MongoBranchRepository repository;
    private final BranchMapper mapper;

    @Override
    public Mono<Branch> save(Branch branch) {
        return Mono.just(branch)
                .map(mapper::toDocument)
                .flatMap(repository::save)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Branch> findById(String id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public Flux<Branch> findByFranchiseId(String franchiseId) {
        return repository.findByFranchiseId(franchiseId)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Branch> update(Branch branch) {
        return save(branch);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}

