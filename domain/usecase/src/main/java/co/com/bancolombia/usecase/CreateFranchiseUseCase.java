package co.com.bancolombia.usecase;

import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Franchise;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class CreateFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String name) {
        log.info("Creating new franchise with name: {}", name);

        return Mono.just(Franchise.create(name))
                .flatMap(franchiseRepository::save)
                .doOnSuccess(franchise -> log.info("Franchise created successfully with id: {}", franchise.getId()))
                .doOnError(error -> log.error("Error creating franchise: {}", error.getMessage()));
    }
}
