package co.com.bancolombia.driven.adapter.mongo;

import co.com.bancolombia.driven.adapter.mongo.mapper.FranchiseMapper;
import co.com.bancolombia.driven.adapter.mongo.repository.MongoFranchiseRepository;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Franchise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseMongoAdapter implements FranchiseRepository {

    private final MongoFranchiseRepository repository;
    private final FranchiseMapper mapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return Mono.just(franchise)
                .map(mapper::toDocument)
                .flatMap(repository::save)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public Flux<Franchise> findAll() {
        return repository.findAll()
                .map(mapper::toModel);
    }

    @Override
    public Mono<Franchise> update(Franchise franchise) {
        return save(franchise);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}
