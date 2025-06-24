package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.model.Franchise;
import co.com.bancolombia.model.log.Logger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final Logger logger;

    public Mono<Franchise> execute(String name) {
        logger.info("Creating new franchise with name: {}", name);

        return Mono.just(Franchise.create(name))
                .flatMap(franchiseRepository::save)
                .doOnSuccess(franchise -> logger.info("Franchise created successfully with id: {}", franchise.getId()))
                .doOnError(error -> logger.error("Error creating franchise: {}", error.getMessage()));
    }
}
